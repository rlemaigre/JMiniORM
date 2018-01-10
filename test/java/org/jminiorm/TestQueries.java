package org.jminiorm;

import org.h2.tools.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestQueries {

    private static Server tcpServer;

    /**
     * This is not mandatory to use in memory H2 databases, but it makes it possible to set a breakpoint (that must only
     * interrupt the current thread, not all of them, otherwise the server becomes unresponsive !) and inspect the in
     * memory tables from an external JDBC client. Useful for debugging.
     *
     * @throws Exception
     */
    @BeforeAll
    public static void startTCPServer() throws Exception {
        tcpServer = Server.createTcpServer();
        tcpServer.start();
    }

    @AfterAll
    public static void stopTCPServer() throws Exception {
        tcpServer.stop();
    }

    @Test
    public void testQueries() throws Exception {
        // Database creation :
        Database db = new Database("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;", "", "");

        // Table creation :
        db.createTable(Bean.class);
        Map<String, Object> map = db.select("show tables").asMap().one();
        assertEquals("BEANS", map.get("table_name"));
        List<Map<String, Object>> list = db.select("show columns from beans").asMap().list();
        assertEquals(8, list.size());

        // Insertion and reselect :
        Bean b1 = new Bean();
        b1.setLocalDate(LocalDate.now());
        b1.setLocalDateTime(LocalDateTime.now());
        b1.setDate(new Date());
        b1.setSomeInt(1);
        b1.setShortText("short text");
        b1.setLongText("long text");
        b1.setBytes("some bytes".getBytes(StandardCharsets.UTF_8));
        db.insert(b1);
        assertNotNull(b1.getId());
        Bean b2 = db.select(Bean.class).one();
        assertTrue(b1.compareWithoutId(b2));

        // Update :
        b2.setShortText("a new short text");
        db.update(b2);
        Bean b3 = db.select(Bean.class).id(b2.getId()).one();
        assertTrue(b2.compareWithoutId(b3));

        // Delete :
        db.delete(b3);
        assertEquals(0, db.select(Bean.class).list().size());

        // Generic selects :
        db.sql("truncate table beans");
        db.insert(Arrays.asList(
                new Bean("b1"),
                new Bean("b2"),
                new Bean("b3")
                )
        );
        Long countLong = db.select("select count(*) from beans").asPrimitive(Long.class).one();
        assertEquals(new Long(3), countLong);
        Integer countInteger = db.select("select count(*) from beans").asPrimitive(Integer.class).one();
        assertEquals(new Integer(3), countInteger);
        String countString = db.select("select count(*) from beans").asPrimitive(String.class).one();
        assertEquals("3", countString);
        Map<String, Map<String, Object>> resultAsIndexedMaps = db.select("select * from beans").asMap().index
                ("short_text");
        assertEquals(3, resultAsIndexedMaps.size());
        assertEquals("b1", resultAsIndexedMaps.get("b1").get("short_text"));
        List<Pojo> pojos = db.select("show columns from beans").asObject(Pojo.class).list();
        assertEquals(8, pojos.size());
        Map<String, List<Pojo>> groups = db.select("show columns from beans").asObject(Pojo.class).group("field");
        assertEquals(8, groups.size());
        assertEquals("VARBINARY(2147483647)", groups.get("BYTES").get(0).getType());

        // ORM select :
        Map<String, Bean> resultAsIndexedObject = db.select(Bean.class).index("shortText");
        assertEquals(3, resultAsIndexedObject.size());
        assertEquals("b1", resultAsIndexedObject.get("b1").getShortText());
    }

}

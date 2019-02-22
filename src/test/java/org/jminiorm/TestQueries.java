package org.jminiorm;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.h2.tools.Server;
import org.jminiorm.executor.BatchStatementExecutor;
import org.jminiorm.executor.DefaultStatementExecutor;
import org.jminiorm.utils.RSUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
    public void testDefaultExecutionMode() throws Exception {
        Database db;
        IDatabaseConfig config;
        config = new DatabaseConfig.Builder()
                .dataSource("jdbc:h2:mem:test-single;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;", "", "")
                .statementExecutor(new DefaultStatementExecutor())
                .build();
        db = new Database(config);
        testQueriesOnDatabase(db);
    }

    @Test
    public void testBatchExecutionMode() throws Exception {
        Database db;
        IDatabaseConfig config;
        config = new DatabaseConfig.Builder()
                .dataSource("jdbc:h2:mem:test-batch;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;", "", "")
                .statementExecutor(new BatchStatementExecutor())
                .build();
        db = new Database(config);
        testQueriesOnDatabase(db);
    }

    // @Test
    // public void testPostgreSQL() throws Exception {
    // Database db;
    // IDatabaseConfig config;
    // config = new DatabaseConfig.Builder()
    // .dataSource("jdbc:postgresql://localhost/jminiorm", "jminiorm", "jminiorm")
    // .statementExecutor(new DefaultStatementExecutor())
    // .dialect(new PostgreSQLDialect())
    // .build();
    // db = new Database(config);
    // testQueriesOnDatabase(db);
    // }

    protected void testQueriesOnDatabase(IDatabase db) throws Exception {
        // Table creation :
        db.dropTable(Bean.class);
        db.createTable(Bean.class);

        // ORM queries :
        testORMQueries(db);

        // Generic selects :
        testGenericQueries(db);
    }

    private void testORMQueries(IDatabase db) throws Exception {
        db.delete(Bean.class).where("1=1");

        // Insert :
        Bean b1 = new Bean();
        b1.setLocalDate(LocalDate.now());
        b1.setLocalDateTime(LocalDateTime.now());
        b1.setDate(new Date());
        b1.setSomeInt(1);
        b1.setSomeBoolean(true);
        b1.setShortText("short text");
        b1.setLongText("long text");
        b1.setBytes("some bytes".getBytes(StandardCharsets.UTF_8));
        b1.setSubBeans(Arrays.asList(new SubBean(1), new SubBean(2)));
        b1.setTestNameEnum(EnumerationTest.FIRST);
        b1.setTestOrdinalEnum(EnumerationTest.SECOND);
        System.out.println("Created : " + b1);
        db.insert(b1);
        assertNotNull(b1.getId());
        Bean b2 = db.select(Bean.class).one();
        System.out.println("Read in database : " + b2);
        assertTrue(b1.compareWithoutId(b2));
        assertNotSame(b1, b2);

        // Update :
        b2.setShortText("a new short text");
        db.update(b2);
        Bean b3 = db.select(Bean.class).id(b2.getId()).one();
        assertTrue(b2.compareWithoutId(b3));

        // Delete :
        db.delete(b3);
        assertEquals(0, db.select(Bean.class).list().size());

        // ORM select :
        db.sql("truncate table beans");
        db.insert(Arrays.asList(
                new Bean("b1"),
                new Bean("b2"),
                new Bean("b3")));
        Map<String, Bean> resultAsIndexedObject = RSUtils.index(db.select(Bean.class).list(), "shortText");
        assertEquals(3, resultAsIndexedObject.size());
        assertEquals("b1", resultAsIndexedObject.get("b1").getShortText());
    }

    private void testGenericQueries(IDatabase db) throws Exception {
        db.sql("truncate table beans");
        db.insert(Arrays.asList(
                new Bean("b1"),
                new Bean("b2"),
                new Bean("b3")));
        Long countLong = db.select("select count(*) from beans").asPrimitive(Long.class).one();
        assertEquals(new Long(3), countLong);
        Integer countInteger = db.select("select count(*) from beans").asPrimitive(Integer.class).one();
        assertEquals(new Integer(3), countInteger);
        String countString = db.select("select count(*) from beans").asPrimitive(String.class).one();
        assertEquals("3", countString);
        Map<String, Map<String, Object>> resultAsIndexedMaps = RSUtils.index(db.select("select * from beans").asMap()
                .list(), "short_text");
        assertEquals(3, resultAsIndexedMaps.size());
        assertEquals("b1", resultAsIndexedMaps.get("b1").get("short_text"));
    }

    private void testLocalDate(IDatabase db) throws Exception {
        db.sql("truncate table beans");
        Bean b = new Bean();
        b.setLocalDate(LocalDate.now());
        db.insert(b);
        Map<String, Object> data = db.select("select localDate from beans").asMap().one();
        assertEquals(LocalDate.now(), data.get("localDate"));
    }

}

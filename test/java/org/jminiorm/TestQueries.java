package org.jminiorm;

import org.h2.tools.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestQueries {

    private static Server tcpServer;

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
        Map<String, Object> map = db.select("show tables").one();
        assertEquals("BEANS", map.get("table_name"));
        List<Map<String, Object>> list = db.select("show columns from beans").list();
        assertEquals(8, list.size());

        // Insertion and select :
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
    }

}

package org.jminiorm;

import org.h2.tools.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

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
        Assertions.assertEquals("BEANS", map.get("table_name"));
        Map<String, Map<String, Object>> list = db.select("show columns from beans").uniqueIndex("column_name");
    }

}

package com.wiley.beginningspring.ch6;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.h2.tools.RunScript;
import org.h2.tools.Server;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) throws SQLException,
            FileNotFoundException {
        Server server = Server
                .createTcpServer("-tcpPort", "8092", "-tcpAllowOthers").start();

        String databaseURL = "jdbc:h2:tcp://localhost:8092/mem:db1";
        // Properties properties = new Properties();
        File file = new File("db.sql");
        Connection connection = DriverManager
                .getConnection(databaseURL/*, properties*/);
        FileReader fileReader = new FileReader(file);
        RunScript.execute(connection, fileReader);

        AccountService accountService = new AccountServiceJdbcTxImpl();
        accountService.transferMoney(100L, 101L, 5.0d);

        server.stop();
    }
}

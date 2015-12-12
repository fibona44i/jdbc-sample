package com.wiley.beginningspring.ch6;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import org.h2.Driver;

public class AccountServiceJdbcTxImpl implements AccountService {

    public void transferMoney(long sourceAccountId, long targetAccountId,
                              double amount) {
        Connection connection = null;
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager
                    .getConnection("jdbc:h2:tcp://localhost:8092/mem:db1", "",
                                   "");
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "update account set balance = balance - " + amount + " where id = " + sourceAccountId);
            statement.executeUpdate(
                    "update account set balance = balance + " + amount + " where id = " + targetAccountId);

            String sql = "SELECT * FROM account";

            ResultSet rs = statement.executeQuery(sql);

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    System.out.print(rs.getString(i) + " ");
                }
                System.out.println();
            }

            connection.commit();
            System.out.println("success!");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
            }
        }
    }

}

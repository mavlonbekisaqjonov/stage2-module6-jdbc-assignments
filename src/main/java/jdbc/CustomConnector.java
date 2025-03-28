package jdbc;

import java.sql.*;

public class CustomConnector {
    public Connection getConnection(String url) {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            System.out.println("problem with connection");
        }
        return null;
    }

    public Connection getConnection(String url, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println("problem with connection");
        }
        return null;
    }
}

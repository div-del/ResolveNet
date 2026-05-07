package com.resolvenet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Change these to match your MySQL setup
    private static final String URL      = "jdbc:mysql://localhost:3306/resolvenet";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "your_password";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}

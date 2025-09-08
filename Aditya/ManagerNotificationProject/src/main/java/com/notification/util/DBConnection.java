package com.notification.util;
 

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/leavemanagementnotificationsection";
    private static final String USER = "root"; // change to your DB user
    private static final String PASSWORD = "Aditya20@"; // change to your DB password

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

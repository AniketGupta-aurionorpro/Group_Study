package com.notification.util;
 

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/leavemanagementnotificationsection";
    private static final String USER = "root"; // change as per your DB
    private static final String PASSWORD = "Aditya20@"; // change as per your DB

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

package com.notification.util;
 

//import java.sql.Connection;
//import java.sql.DriverManager;
//
//public class DBConnection {
//    private static final String URL = "jdbc:mysql://localhost:3306/leave_management";
//    private static final String USER = "root";
//    private static final String PASSWORD = "Aditya20@"; // change if needed
//
//    public static Connection getConnection() {
//        Connection conn = null;
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            conn = DriverManager.getConnection(URL, USER, PASSWORD);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return conn;
//    }
//}


 

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/leave_managementnoti";
    private static final String USER = "root";   // change if needed
    private static final String PASSWORD = "Aditya20@"; // change if needed

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

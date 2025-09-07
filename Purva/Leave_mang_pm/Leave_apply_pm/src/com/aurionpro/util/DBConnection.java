package com.aurionpro.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnection {
    private static DataSource dataSource;

    // Static block to initialize the DataSource
    static {
        try {
            Context initialContext = new InitialContext();
            Context envContext = (Context) initialContext.lookup("java:comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/LeaveManagementDB");
        } catch (NamingException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("JNDI lookup failed: " + e.getMessage());
        }
    }

    // Method to get a Connection from the DataSource pool
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

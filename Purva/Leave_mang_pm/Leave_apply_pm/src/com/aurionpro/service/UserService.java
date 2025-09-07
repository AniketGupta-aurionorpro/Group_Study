//package com.aurionpro.service;
//
//import java.sql.SQLException;
//
//import com.aurionpro.dao.UserDAO;
//import com.aurionpro.model.User;
//
//public class UserService {
//    private UserDAO userDAO;
//
//    public UserService() {
//        this.userDAO = new UserDAO();
//    }
//
//    public User getAdminUser() throws SQLException {
//        // Here you would call a DAO method to get the admin user
//        return userDAO.getAdmin();
//    }
//}
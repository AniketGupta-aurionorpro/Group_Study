//package com.aurionpro.dao;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import com.aurionpro.model.User;
//import com.aurionpro.model.User.UserRole;
//import com.aurionpro.util.DBConnection;
//
//public class UserDAO {
//    public User getAdmin() throws SQLException {
//        String sql = "SELECT id, name, email FROM users WHERE role = ?";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, UserRole.ADMIN.toString());
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    User admin = new User();
//                    admin.setId(rs.getInt("id"));
//                    admin.setName(rs.getString("name"));
//                    admin.setEmail(rs.getString("email"));
//                    return admin;
//                }
//            }
//        }
//        return null;
//    }
//}

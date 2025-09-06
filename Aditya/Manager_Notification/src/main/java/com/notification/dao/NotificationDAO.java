package com.notification.dao;
 
//
//import com.notification.model.Notification;
//import com.notification.util.DBConnection;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class NotificationDAO {
//    public void addNotification(int receiverId, String message) {
//        String sql = "INSERT INTO notifications (receiver_id, message) VALUES (?, ?)";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, receiverId);
//            ps.setString(2, message);
//            ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public List<Notification> getNotifications(int receiverId) {
//        List<Notification> list = new ArrayList<>();
//        String sql = "SELECT * FROM notifications WHERE receiver_id=? ORDER BY created_on DESC";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, receiverId);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Notification n = new Notification();
//                n.setId(rs.getInt("id"));
//                n.setReceiverId(rs.getInt("receiver_id"));
//                n.setMessage(rs.getString("message"));
//                n.setSeen(rs.getBoolean("seen"));
//                n.setCreatedOn(rs.getTimestamp("created_on"));
//                list.add(n);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//}






















 

import com.notification.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public void createNotification(int receiverId, String message) {
        String sql = "INSERT INTO notifications (receiver_id, message) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, receiverId);
            ps.setString(2, message);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getNotificationsForManager(int managerId) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT message, created_on FROM notifications WHERE receiver_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("created_on") + " - " + rs.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

package com.notification.dao;
 

import com.notification.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public void createNotification(int receiverId, String message){
        String sql = "INSERT INTO notifications (receiver_id, message) VALUES (?,?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, receiverId);
            ps.setString(2, message);
            ps.executeUpdate();
        } catch(Exception e){ e.printStackTrace(); }
    }

    public List<String> getNotificationsForManager(int managerId){
        List<String> list = new ArrayList<>();
        String sql = "SELECT message, created_on FROM notifications WHERE receiver_id=?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(rs.getString("created_on") + " - " + rs.getString("message"));
            }
        } catch(Exception e){ e.printStackTrace(); }
        return list;
    }
}

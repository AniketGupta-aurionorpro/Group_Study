package com.aurionpro.leave_management.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

public class NotificationDAO {

	private DataSource dataSource;

	public NotificationDAO(DataSource datasource) {
		this.dataSource = datasource;
	}

	public int getUnseenNotificationCount(int receiverId) {
		String sql = "SELECT COUNT(*) FROM notifications WHERE receiver_id = ? AND seen = FALSE";
		try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, receiverId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int notifications(int receiverId) {
		String sql = "SELECT *FROM notifications WHERE receiver_id = ? AND seen = FALSE";
		try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, receiverId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Map<String, Object>> getNotificationsByReceiverId(int receiverId) {
	    List<Map<String, Object>> notifications = new ArrayList<>();
	    String sql = "SELECT id, message, seen, created_on " +
	                 "FROM notifications " +
	                 "WHERE receiver_id = ? " +
	                 "ORDER BY created_on DESC";

	    try (Connection con = dataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, receiverId);

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Map<String, Object> notif = new HashMap<>();
	                notif.put("id", rs.getInt("id"));
	                notif.put("message", rs.getString("message"));
	                notif.put("seen", rs.getBoolean("seen"));
	                notif.put("created_on", rs.getTimestamp("created_on"));
	                notifications.add(notif);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return notifications;
	}
	
	public boolean markAsRead(int id) {
	    String sql = "UPDATE notifications SET seen = TRUE WHERE id = ?";
	    try (Connection con = dataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, id);
	        return ps.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	public boolean deleteNotification(int id) {
	    String sql = "DELETE FROM notifications WHERE id = ?";
	    try (Connection con = dataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, id);
	        return ps.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	public boolean markAllAsRead(int receiverId) {
	    String sql = "UPDATE notifications SET seen = TRUE WHERE receiver_id = ?";
	    try (Connection con = dataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, receiverId);
	        return ps.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	public boolean deleteAllRead(int receiverId) {
	    String sql = "DELETE FROM notifications WHERE receiver_id = ? AND seen = TRUE";
	    try (Connection con = dataSource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setInt(1, receiverId);
	        return ps.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

}

package com.aurionpro.Dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.aurionpro.model.Leave;
import com.aurionpro.model.User;

public class LeaveDAO {

	private DataSource datasource;

	public LeaveDAO(DataSource datasource) {
		this.datasource = datasource;
	}

	public List<Leave> getPendingRequests() {
		List<Leave> list = new ArrayList<>();
		try (Connection con = datasource.getConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * FROM leaves WHERE status='PENDING' ORDER BY applied_on DESC")) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Leave l = new Leave();
				l.setId(rs.getInt("id"));
				l.setUserId(rs.getInt("user_id"));
				l.setStartDate(rs.getDate("start_date"));
				l.setEndDate(rs.getDate("end_date"));
				l.setReason(rs.getString("reason"));
				l.setStatus(rs.getString("status"));
				list.add(l);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Leave> getApprovedRequests() {
		List<Leave> list = new ArrayList<>();
		try (Connection con = datasource.getConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * FROM leaves WHERE status='APPROVED' ORDER BY applied_on DESC")) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Leave l = new Leave();
				l.setId(rs.getInt("id"));
				l.setUserId(rs.getInt("user_id"));
				l.setStartDate(rs.getDate("start_date"));
				l.setEndDate(rs.getDate("end_date"));
				l.setReason(rs.getString("reason"));
				l.setStatus(rs.getString("status"));
				list.add(l);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Leave> getLeavesByDate(String date) {
		List<Leave> leaves = new ArrayList<>();
		String sql = "SELECT l.id, l.reason, u.id AS userId, u.name, u.role "
				+ "FROM leaves l JOIN users u ON l.user_id = u.id "
				+ "WHERE ? BETWEEN l.start_date AND l.end_date AND l.status='APPROVED'";

		try (Connection con = datasource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, date);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Leave leave = new Leave();
					leave.setId(rs.getInt("id"));
					leave.setReason(rs.getString("reason"));

					User user = new User();
					user.setId(rs.getInt("userId"));
					user.setName(rs.getString("name"));
					user.setRole(rs.getString("role"));

					leave.setUser(user);
					leaves.add(leave);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return leaves;
	}

	public boolean updateLeaveStatus(int leaveId, String status, int adminId) {
	    String sql = "UPDATE leaves SET status=?, approved_by=?, approved_on=CURRENT_TIMESTAMP WHERE id=?";
	    try (Connection con = datasource.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, status);
	        ps.setInt(2, adminId);
	        ps.setInt(3, leaveId);
	        return ps.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	public boolean addLeaveNotification(int leaveId, String status) {
	    String getLeaveSql = "SELECT user_id, start_date, end_date FROM leaves WHERE id=?";
	    String insertNotifSql = "INSERT INTO notifications (receiver_id, message) VALUES (?, ?)";

	    try (Connection con = datasource.getConnection();
	         PreparedStatement ps1 = con.prepareStatement(getLeaveSql)) {

	        // Step 1: get leave details
	        ps1.setInt(1, leaveId);
	        try (ResultSet rs = ps1.executeQuery()) {
	            if (rs.next()) {
	                int userId = rs.getInt("user_id");
	                Date start = rs.getDate("start_date");
	                Date end = rs.getDate("end_date");

	                // Step 2: build message with period
	                String message = String.format(
	                        "Your leave request (ID %d) from %s to %s has been %s.",
	                        leaveId, start.toString(), end.toString(), status
	                );

	                // Step 3: insert notification
	                try (PreparedStatement ps2 = con.prepareStatement(insertNotifSql)) {
	                    ps2.setInt(1, userId);
	                    ps2.setString(2, message);
	                    return ps2.executeUpdate() > 0;
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

}

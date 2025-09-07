package com.aurionpro.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.model.Leave;
import com.aurionpro.util.DBConnection;

public class LeaveDAO {

    public void applyLeave(Leave leave) throws SQLException {
        String sql = "INSERT INTO leaves (user_id, start_date, end_date, reason, status, approved_by) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, leave.getUserId());
            stmt.setDate(2, new Date(leave.getStartDate().getTime()));
            stmt.setDate(3, new Date(leave.getEndDate().getTime()));
            stmt.setString(4, leave.getReason());
            stmt.setString(5, "PENDING");
            
            if (leave.getApprovedBy() == null) {
                stmt.setNull(6, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(6, leave.getApprovedBy());
            }

            stmt.executeUpdate();
        }
    }
    
    public List<Leave> getLeavesByUserId(int userId) throws SQLException {
        List<Leave> leaves = new ArrayList<>();
        String sql = "SELECT * FROM leaves WHERE user_id = ? ORDER BY applied_on DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Leave leave = new Leave();
                    leave.setId(rs.getInt("id"));
                    leave.setUserId(rs.getInt("user_id"));
                    leave.setStartDate(rs.getDate("start_date"));
                    leave.setEndDate(rs.getDate("end_date"));
                    leave.setReason(rs.getString("reason"));
                    leave.setStatus(rs.getString("status"));
                    leave.setAppliedOn(rs.getTimestamp("applied_on"));
                    leave.setApprovedBy(rs.getInt("approved_by"));
                    leave.setApprovedOn(rs.getTimestamp("approved_on"));
                    leave.setRejectionReason(rs.getString("rejection_reason"));
                    leaves.add(leave);
                }
            }
        }
        return leaves;
    }

    public List<Leave> getLeavesByDateRange(int userId, Date startDate, Date endDate) throws SQLException {
        List<Leave> leaves = new ArrayList<>();
        String sql = "SELECT * FROM leaves WHERE user_id = ? AND (start_date BETWEEN ? AND ?) ORDER BY applied_on DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, startDate);
            stmt.setDate(3, endDate);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Leave leave = new Leave();
                    leave.setId(rs.getInt("id"));
                    leave.setUserId(rs.getInt("user_id"));
                    leave.setStartDate(rs.getDate("start_date"));
                    leave.setEndDate(rs.getDate("end_date"));
                    leave.setReason(rs.getString("reason"));
                    leave.setStatus(rs.getString("status"));
                    leave.setAppliedOn(rs.getTimestamp("applied_on"));
                    leave.setApprovedBy(rs.getInt("approved_by"));
                    leave.setApprovedOn(rs.getTimestamp("approved_on"));
                    leave.setRejectionReason(rs.getString("rejection_reason"));
                    leaves.add(leave);
                }
            }
        }
        return leaves;
    }
    
    public int getPendingLeavesCount(int userId) throws SQLException {
        int count = 0;
        String sql = "SELECT SUM(DATEDIFF(end_date, start_date) + 1) FROM leaves WHERE user_id = ? AND status = 'PENDING'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        }
        return count;
    }
}
package com.aurionpro.leave_management.repository;


import com.aurionpro.leave_management.model.LeaveBalance;
import com.aurionpro.leave_management.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;

public class LeaveBalanceDao {
    public LeaveBalance findByUserId(int userId) {
        String sql = "SELECT total_yearly_leave, yearly_leave_taken FROM leave_balance WHERE user_id = ? AND year = ?";
        LeaveBalance balance = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, LocalDate.now().getYear());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                balance = new LeaveBalance();
                balance.setTotalYearlyLeave(rs.getInt("total_yearly_leave"));
                balance.setYearlyLeaveTaken(rs.getInt("yearly_leave_taken"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }
    public LeaveBalance findByUserIdAndYear(int userId, int year) {
        String sql = "SELECT * FROM leave_balance WHERE user_id = ? AND year = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, year);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLeaveBalance(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void incrementLeaveTaken(int userId, int year, int duration) {
        String sql = "UPDATE leave_balance SET yearly_leave_taken = yearly_leave_taken + ? WHERE user_id = ? AND year = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, duration);
            stmt.setInt(2, userId);
            stmt.setInt(3, year);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createInitialBalance(int userId) {
        // This will use the default value of 24 for total_yearly_leave
        String sql = "INSERT INTO leave_balance (user_id, year) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, LocalDate.now().getYear());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private LeaveBalance mapResultSetToLeaveBalance(ResultSet rs) throws SQLException {
        LeaveBalance balance = new LeaveBalance();
        balance.setId(rs.getInt("id"));
        balance.setUserId(rs.getInt("user_id"));
        balance.setYear(rs.getInt("year"));
        balance.setTotalYearlyLeave(rs.getInt("total_yearly_leave"));
        balance.setYearlyLeaveTaken(rs.getInt("yearly_leave_taken"));
        return balance;
    }

    public LeaveBalance getLeaveBalanceByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM leave_balance WHERE user_id = ? AND year = YEAR(CURRENT_DATE())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LeaveBalance balance = new LeaveBalance();
                    balance.setId(rs.getInt("id"));
                    balance.setUserId(rs.getInt("user_id"));
                    balance.setYear(rs.getInt("year"));
                    balance.setYearlyLeaveTaken(rs.getInt("yearly_leave_taken"));
                    balance.setTotalYearlyLeave(rs.getInt("total_yearly_leave"));
//                    balance.setMonthlyLeaveTaken(rs.getInt("monthly_leave_taken"));
//                    balance.setTotalMonthlyLeave(rs.getInt("total_monthly_leave"));
                    return balance;
                }
            }
        }
        return null;
    }
}
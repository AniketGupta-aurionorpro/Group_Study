package com.aurionpro.dao;

import com.aurionpro.model.LeaveBalance; // Add this line
import com.aurionpro.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LeaveBalanceDAO {
    public LeaveBalance getLeaveBalanceByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM leave_balance WHERE user_id = ? AND year = YEAR(CURRENT_DATE())";
        try (Connection conn = DBConnection.getConnection();
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
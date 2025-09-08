package com.aurionpro.leave_management.repository;


import com.aurionpro.leave_management.model.Leave;
import com.aurionpro.leave_management.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for handling all database operations related to the 'leaves' table.
 */
public class LeaveDao {

    /**
     * Creates a new leave application in the database.
     * The status defaults to 'PENDING' and applied_on to the current time via the database schema.
     *
     * @param leave The Leave object containing user_id, start_date, end_date, and reason.
     */
    public void createLeave(Leave leave) {
        String sql = "INSERT INTO leaves (user_id, start_date, end_date, reason) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, leave.getUserId());
            stmt.setDate(2, java.sql.Date.valueOf(leave.getStartDate()));
            stmt.setDate(3, java.sql.Date.valueOf(leave.getEndDate()));
            stmt.setString(4, leave.getReason());

            stmt.executeUpdate();
        } catch (SQLException e) {
            // In a real application, use a logging framework like SLF4J or Log4j
            e.printStackTrace();
        }
    }

    /**
     * Updates the status of an existing leave request.
     * Typically used by a manager to approve or reject a leave.
     *
     * @param leaveId     The ID of the leave to update.
     * @param status      The new status ('APPROVED' or 'REJECTED').
     * @param approverId  The ID of the user (manager/admin) who is approving/rejecting.
     */
    public void updateLeaveStatus(int leaveId, String status, int approverId) {
        String sql = "UPDATE leaves SET status = ?, approved_by = ?, approved_on = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, approverId);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(4, leaveId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds a single leave by its primary key ID.
     *
     * @param leaveId The ID of the leave to find.
     * @return A Leave object if found, otherwise null.
     */
    public Leave findById(int leaveId) {
        String sql = "SELECT * FROM leaves WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, leaveId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLeave(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all leave applications for a specific user.
     *
     * @param userId The ID of the user whose leaves are to be retrieved.
     * @return A list of Leave objects.
     */
    public List<Leave> findByUserId(int userId) {
        List<Leave> leaves = new ArrayList<>();
        String sql = "SELECT * FROM leaves WHERE user_id = ? ORDER BY start_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    leaves.add(mapResultSetToLeave(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaves;
    }

    /**
     * Retrieves all pending leave requests for employees who report to a specific manager.
     *
     * @param managerId The ID of the manager.
     * @return A list of pending Leave objects for the manager's direct reports.
     */
    public List<Leave> findPendingLeavesForManager(int managerId) {
        List<Leave> leaves = new ArrayList<>();
        // This query joins the leaves table with the users table to find employees
        // who have the specified manager_id and a pending leave request.
        String sql = "SELECT l.* FROM leaves l " +
                "JOIN users u ON l.user_id = u.id " +
                "WHERE u.manager_id = ? AND l.status = 'PENDING' " +
                "ORDER BY l.applied_on ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, managerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    leaves.add(mapResultSetToLeave(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaves;
    }

    /**
     * Deletes a leave application from the database.
     * Can be used by an employee to cancel a pending request.
     *
     * @param leaveId The ID of the leave to delete.
     */
    public void delete(int leaveId) {
        String sql = "DELETE FROM leaves WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, leaveId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Helper method to map a row from a ResultSet to a Leave object.
     * This avoids code duplication in methods that retrieve leave data.
     *
     * @param rs The ResultSet object, already positioned at the correct row.
     * @return A populated Leave object.
     * @throws SQLException if a database access error occurs.
     */
    private Leave mapResultSetToLeave(ResultSet rs) throws SQLException {
        Leave leave = new Leave();
        leave.setId(rs.getInt("id"));
        leave.setUserId(rs.getInt("user_id"));
        leave.setStartDate(rs.getDate("start_date").toLocalDate());
        leave.setEndDate(rs.getDate("end_date").toLocalDate());
        leave.setReason(rs.getString("reason"));
        leave.setStatus(rs.getString("status"));
        leave.setAppliedOn(Timestamp.valueOf(rs.getTimestamp("applied_on").toLocalDateTime()));

        // Handle nullable columns safely
        leave.setApprovedBy((Integer) rs.getObject("approved_by"));

        Timestamp approvedOnTs = rs.getTimestamp("approved_on");
        if (approvedOnTs != null) {
            leave.setApprovedOn(Timestamp.valueOf(approvedOnTs.toLocalDateTime()));
        } else {
            leave.setApprovedOn(null);
        }

        return leave;
    }
    public boolean hasOverlappingLeave(int userId, LocalDate startDate, LocalDate endDate) {
        // The SQL query checks for any leave where the ranges overlap and the status is not 'REJECTED'.
        // The condition (start_date <= ? AND end_date >= ?) is the standard way to check for overlapping date ranges.
        String sql = "SELECT COUNT(*) FROM leaves WHERE user_id = ? AND status != 'REJECTED' AND start_date <= ? AND end_date >= ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setDate(2, java.sql.Date.valueOf(endDate));   // new endDate
            stmt.setDate(3, java.sql.Date.valueOf(startDate)); // new startDate

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // If the count is greater than 0, it means an overlapping leave was found.
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Default to false in case of an error to be safe, though logging the error is important.
        return false;
    }
    
    public void applyLeave(Leave leave) throws SQLException {
        String sql = "INSERT INTO leaves (user_id, start_date, end_date, reason) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Use the correct methods to set both start_date and end_date
            stmt.setInt(1, leave.getUserId());
            stmt.setDate(2, java.sql.Date.valueOf(leave.getStartDate())); // Correctly sets start_date
            stmt.setDate(3, java.sql.Date.valueOf(leave.getEndDate()));   // Correctly sets end_date
            stmt.setString(4, leave.getReason());

            stmt.executeUpdate();
        }
    }
    
    public List<Leave> getLeavesByUserId(int userId) throws SQLException {
        List<Leave> leaves = new ArrayList<>();
        String sql = "SELECT * FROM leaves WHERE user_id = ? ORDER BY applied_on DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Leave leave = new Leave();
                    leave.setId(rs.getInt("id"));
                    leave.setUserId(rs.getInt("user_id"));
                    leave.setStartDate(rs.getDate("start_date").toLocalDate());
                    leave.setEndDate(rs.getDate("end_date").toLocalDate());
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

    public List<Leave> getLeavesByDateRange(int userId, LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Leave> leaves = new ArrayList<>();
        String sql = "SELECT * FROM leaves WHERE user_id = ? AND (start_date BETWEEN ? AND ?) ORDER BY applied_on DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Leave leave = new Leave();
                    leave.setId(rs.getInt("id"));
                    leave.setUserId(rs.getInt("user_id"));
                    leave.setStartDate(rs.getDate("start_date").toLocalDate());
                    leave.setEndDate(rs.getDate("end_date").toLocalDate());
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
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);//gets the value from the first column of the current row.
                }
            }
        }
        return count;
    }
}

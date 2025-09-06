package com.notification.dao;
 
//
//import com.notification.model.Leave;
//import com.notification.util.DBConnection;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class LeaveDAO {
//    public List<Leave> getPendingLeaves(int managerId) {
//        List<Leave> list = new ArrayList<>();
//        String sql = "SELECT l.*, u.name FROM leaves l " +
//                     "JOIN users u ON l.user_id=u.id " +
//                     "WHERE u.manager_id=? AND l.status='PENDING'";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, managerId);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Leave leave = new Leave();
//                leave.setId(rs.getInt("id"));
//                leave.setUserId(rs.getInt("user_id"));
//                leave.setStartDate(rs.getString("start_date"));
//                leave.setEndDate(rs.getString("end_date"));
//                leave.setReason(rs.getString("reason"));
//                leave.setStatus(rs.getString("status"));
//                leave.setAppliedOn(rs.getTimestamp("applied_on"));
//                list.add(leave);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    public void updateLeaveStatus(int leaveId, String status, String rejectionReason, int managerId) {
//        String sql = "UPDATE leaves SET status=?, rejection_reason=?, approved_by=?, approved_on=NOW() WHERE id=?";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, status);
//            ps.setString(2, rejectionReason);
//            ps.setInt(3, managerId);
//            ps.setInt(4, leaveId);
//            ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}






















 

import com.notification.model.Leave;
import com.notification.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaveDAO {

    public List<Leave> getPendingLeavesForManager(int managerId) {
        List<Leave> leaves = new ArrayList<>();
        String sql = "SELECT l.id, l.user_id, u.name, l.start_date, l.end_date, l.reason, l.status " +
                     "FROM leaves l JOIN users u ON l.user_id = u.id " +
                     "WHERE u.manager_id = ? AND l.status = 'PENDING'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Leave leave = new Leave();
                leave.setId(rs.getInt("id"));
                leave.setUserId(rs.getInt("user_id"));
                leave.setEmployeeName(rs.getString("name"));
                leave.setStartDate(rs.getString("start_date"));
                leave.setEndDate(rs.getString("end_date"));
                leave.setReason(rs.getString("reason"));
                leave.setStatus(rs.getString("status"));
                leaves.add(leave);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return leaves;
    }

    public void updateLeaveStatus(int leaveId, String status, int managerId, String rejectionReason) {
        String sql = "UPDATE leaves SET status=?, approved_by=?, approved_on=NOW(), rejection_reason=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, managerId);
            ps.setString(3, rejectionReason);
            ps.setInt(4, leaveId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getManagerAttendance(int managerId) {
        List<String> attendanceList = new ArrayList<>();
        String sql = "SELECT date, status FROM attendance WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                attendanceList.add(rs.getString("date") + " - " + rs.getString("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attendanceList;
    }
}

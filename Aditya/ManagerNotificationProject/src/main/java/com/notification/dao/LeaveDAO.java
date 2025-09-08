package com.notification.dao;
 

import com.notification.model.Leave;
import com.notification.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaveDAO {

    public List<Leave> getPendingLeavesForManager(int managerId, String filterName, String filterId) {
        List<Leave> leaves = new ArrayList<>();
        String sql = "SELECT l.id, l.user_id, u.name, l.start_date, l.end_date, l.reason, l.status " +
                     "FROM leaves l JOIN users u ON l.user_id = u.id " +
                     "WHERE u.manager_id=? AND l.status='PENDING'";

        if(filterName != null && !filterName.isEmpty()) sql += " AND u.name LIKE ?";
        if(filterId != null && !filterId.isEmpty()) sql += " AND u.id = ?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, managerId);
            int idx = 2;
            if(filterName != null && !filterName.isEmpty()) ps.setString(idx++, "%" + filterName + "%");
            if(filterId != null && !filterId.isEmpty()) ps.setInt(idx, Integer.parseInt(filterId));

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
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

        } catch(Exception e){ e.printStackTrace(); }

        return leaves;
    }

    public void updateLeaveStatus(int leaveId, String status, int managerId, String rejectionReason) {
        String sql = "UPDATE leaves SET status=?, approved_by=?, approved_on=NOW(), rejection_reason=? WHERE id=?";
        String updateBalance = "UPDATE leave_balance lb JOIN leaves l ON lb.user_id=l.user_id " +
                               "SET lb.yearly_leave_taken = lb.yearly_leave_taken + DATEDIFF(l.end_date, l.start_date)+1 " +
                               "WHERE l.id=? AND ?='APPROVED'";

        try(Connection conn = DBConnection.getConnection()) {
            try(PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, status);
                ps.setInt(2, managerId);
                ps.setString(3, rejectionReason);
                ps.setInt(4, leaveId);
                ps.executeUpdate();
            }

            try(PreparedStatement ps2 = conn.prepareStatement(updateBalance)) {
                ps2.setInt(1, leaveId);
                ps2.setString(2, status);
                ps2.executeUpdate();
            }

        } catch(Exception e){ e.printStackTrace(); }
    }
}

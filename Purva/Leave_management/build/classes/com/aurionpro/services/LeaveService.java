package com.aurionpro.service;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import com.aurionpro.dao.LeaveDAO;
import com.aurionpro.model.Leave;

public class LeaveService {

    private LeaveDAO leaveDAO;

    public LeaveService() {
        this.leaveDAO = new LeaveDAO();
    }

    public void applyLeave(Leave leave) throws SQLException {
        // Business logic and validation can go here
        // For example, check if dates are valid, if the user has enough leaves, etc.
        // For now, we will just call the DAO to save the data.

        // This is a crucial step: ensure the Leave object has the managerId
        // The controller should get this from the user session object and set it.
        if (leave.getManagerId() == null || leave.getManagerId() == 0) {
            throw new IllegalArgumentException("Manager ID is required to apply for leave.");
        }

        leaveDAO.applyLeave(leave);
    }
    
    // New method to retrieve leave history from the DAO
    public List<Leave> getLeavesByUserId(int userId) throws SQLException {
        return leaveDAO.getLeavesByUserId(userId);
    }
    
 // New method to fetch leaves by a date range
    public List<Leave> getLeavesByDateRange(int userId, Date startDate, Date endDate) throws SQLException {
        return leaveDAO.getLeavesByDateRange(userId, startDate, endDate);
    }
 // New method to count pending leaves
    public int getPendingLeavesCount(int userId) throws SQLException {
        return leaveDAO.getPendingLeavesCount(userId);
    }
    
}

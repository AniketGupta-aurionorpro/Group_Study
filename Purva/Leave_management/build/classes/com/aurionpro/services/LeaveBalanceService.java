package com.aurionpro.service;

import com.aurionpro.dao.LeaveBalanceDAO;
import com.aurionpro.dao.LeaveDAO;
import com.aurionpro.model.LeaveBalance;

import java.sql.SQLException;

public class LeaveBalanceService {
    private LeaveBalanceDAO leaveBalanceDAO;

    public LeaveBalanceService() {
        this.leaveBalanceDAO = new LeaveBalanceDAO();
    }

    // This is the method the LeaveController is trying to call
    public LeaveBalance getLeaveBalance(int userId) throws SQLException {
        return leaveBalanceDAO.getLeaveBalanceByUserId(userId);
    }
    
 
}
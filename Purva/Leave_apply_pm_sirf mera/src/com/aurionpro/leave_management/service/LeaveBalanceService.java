package com.aurionpro.leave_management.service;


import com.aurionpro.leave_management.model.LeaveBalance;
import com.aurionpro.leave_management.repository.LeaveBalanceDao;

import java.sql.SQLException;

public class LeaveBalanceService {
    private LeaveBalanceDao leaveBalanceDAO;

    public LeaveBalanceService() {
        this.leaveBalanceDAO = new LeaveBalanceDao();
    }

    // This is the method the LeaveController is trying to call
    public LeaveBalance getLeaveBalance(int userId) throws SQLException {
        return leaveBalanceDAO.getLeaveBalanceByUserId(userId);
    }
    
 
}
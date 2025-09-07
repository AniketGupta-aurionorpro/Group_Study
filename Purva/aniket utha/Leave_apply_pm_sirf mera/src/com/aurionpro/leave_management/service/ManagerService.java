package com.aurionpro.leave_management.service;



import com.aurionpro.leave_management.model.User;

import java.util.List;

public interface ManagerService {
    List<User> getEmployeesByManagerId(int managerId) throws Exception;
}


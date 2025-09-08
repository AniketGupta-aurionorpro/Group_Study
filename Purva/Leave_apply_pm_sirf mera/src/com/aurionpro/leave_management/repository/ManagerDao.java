package com.aurionpro.leave_management.repository;




import com.aurionpro.leave_management.model.User;

import java.util.List;

public interface ManagerDao {
    List<User> getEmployeesByManagerId(int managerId) throws Exception;
}

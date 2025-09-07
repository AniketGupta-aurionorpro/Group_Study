package com.aurionpro.service;


import com.aurionpro.model.User;
import java.util.List;

public interface ManagerService {
    List<User> getEmployeesByManagerId(int managerId) throws Exception;
}


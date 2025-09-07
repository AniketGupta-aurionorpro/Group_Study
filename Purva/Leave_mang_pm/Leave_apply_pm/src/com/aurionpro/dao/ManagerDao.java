package com.aurionpro.dao;



import com.aurionpro.model.User;
import java.util.List;

public interface ManagerDao {
    List<User> getEmployeesByManagerId(int managerId) throws Exception;
}

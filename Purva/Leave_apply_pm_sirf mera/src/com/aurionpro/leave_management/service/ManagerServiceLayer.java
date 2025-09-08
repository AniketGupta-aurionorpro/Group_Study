package com.aurionpro.leave_management.service;



import com.aurionpro.leave_management.model.User;
import com.aurionpro.leave_management.repository.ManagerDao;
import com.aurionpro.leave_management.repository.ManagerDaoImpl;

import javax.sql.DataSource;
import java.util.List;

public class ManagerServiceLayer implements ManagerService {
    private ManagerDao managerDao;

    public ManagerServiceLayer() {
        this.managerDao = new ManagerDaoImpl();
    }

    @Override
    public List<User> getEmployeesByManagerId(int managerId) throws Exception {
        return managerDao.getEmployeesByManagerId(managerId);
    }
}


package com.aurionpro.service;

import com.aurionpro.dao.ManagerDao;
import com.aurionpro.dao.ManagerDaoImpl;
import com.aurionpro.model.User;

import javax.sql.DataSource;
import java.util.List;

public class ManagerServiceLayer implements ManagerService {
    private ManagerDao managerDao;

    public ManagerServiceLayer(DataSource dataSource) {
        this.managerDao = new ManagerDaoImpl(dataSource);
    }

    @Override
    public List<User> getEmployeesByManagerId(int managerId) throws Exception {
        return managerDao.getEmployeesByManagerId(managerId);
    }
}


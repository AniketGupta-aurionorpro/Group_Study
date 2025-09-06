package com.aurionpro.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.aurionpro.Dao.LeaveDAO;
import com.aurionpro.Dao.NotificationDAO;
import com.aurionpro.Dao.UserDAO;
import com.aurionpro.model.Leave;
import com.aurionpro.model.User;

import jakarta.annotation.Resource;

public class AdminService {
	private UserDAO userDao;
	private LeaveDAO leaveDao;
	private NotificationDAO notifDao;
	
	
	@Resource(name = "jdbc/db-source")
    private DataSource dataSource;
	
	
	public AdminService(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
		this.leaveDao = new LeaveDAO(dataSource);
		this.userDao = new UserDAO(dataSource);
		this.notifDao = new NotificationDAO(dataSource);
	}


	public List<Leave> getPendingRequestManagers() {
		List<Leave> pendingRequests = leaveDao.getPendingRequests();
		List<Leave> pendingRequestManagers = new ArrayList<>();
		for (Leave l: pendingRequests) {
			User user = userDao.getManagerById(l.getUserId());
			if(user != null)
			{
				l.setUser(user);
				pendingRequestManagers.add(l);
			}
		}
		return pendingRequestManagers;
		
	}
	
	public Map<Integer,List<User>> getApprovedLeaves(List<Leave> list){
		Map<Integer, List<User>> approvedLeaves = new HashMap<>();
		for(Leave l:list) {
			List<User> approvedLeaveUsers = userDao.getApprovedLeaveUsers(l);
			approvedLeaves.put(l.getId(), approvedLeaveUsers);
		}
		return approvedLeaves;
		
	}


	public int getPendingRequestManagersCount() {
		List<Leave> pendingRequests = leaveDao.getPendingRequests();
		int count=0;
		for (Leave l: pendingRequests) {
			User user = userDao.getManagerById(l.getUserId());
			if(user != null)
			{
				l.setUser(user);
				count++;
			}
		}
		return count;
		
	}


	public int getNotificationCount(int recieverId) {
		int count =0;
		count = notifDao.getUnseenNotificationCount(recieverId);
		return count;
	}
	
	public List<Map<String, Object>> getNotifications(int recieverId) {
		return notifDao.getNotificationsByReceiverId(recieverId);
	}

}

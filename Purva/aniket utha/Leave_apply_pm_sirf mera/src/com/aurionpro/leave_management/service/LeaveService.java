package com.aurionpro.leave_management.service;



import com.aurionpro.leave_management.model.Leave;
import com.aurionpro.leave_management.repository.LeaveBalanceDao;
import com.aurionpro.leave_management.repository.LeaveDao;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;

import java.time.LocalDate;
import java.util.List;

public class LeaveService {

    private final LeaveDao leaveDao = new LeaveDao();
    private final LeaveBalanceDao leaveBalanceDao = new LeaveBalanceDao();

    /**
     * Processes a new leave application.
     * It performs validation before attempting to save the leave.
     *
     * @param leave The Leave object to be saved.
     * @throws IllegalArgumentException if the leave data is invalid.
     */
    public void applyForLeave(Leave leave) {
        // --- Business Rule Validation ---

        if (leave.getStartDate() == null || leave.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null.");
        }
        if (leave.getStartDate().isAfter(leave.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after the end date.");
        }
        if (leave.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Leave cannot be applied for a past date.");
        }
        if (leave.getReason() == null || leave.getReason().trim().isEmpty()) {
            throw new IllegalArgumentException("A reason for the leave must be provided.");
        }
        if (leaveDao.hasOverlappingLeave(leave.getUserId(), leave.getStartDate(), leave.getEndDate())) {
            throw new IllegalArgumentException("You already have a leave application that overlaps with these dates.");
        }
        leaveDao.createLeave(leave);
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

        leaveDao.applyLeave(leave);
    }

    public void approveLeave(int leaveId, int approverId) {
        // 1. Fetch the leave application
        Leave leave = leaveDao.findById(leaveId);
        if (leave == null) {
            throw new IllegalStateException("Leave request with ID " + leaveId + " not found.");
        }
        if (!"PENDING".equals(leave.getStatus())) {
            throw new IllegalStateException("Leave request is not in a PENDING state and cannot be approved.");
        }
        // 2. Calculate the duration of the leave in days
        // ChronoUnit.DAYS.between is inclusive of the start date and exclusive of the end date.
        // So we add 1 to get the total number of days.
        long durationInDays = ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;

        // Use the year of the leave's start date for the balance update
        int yearOfLeave = leave.getStartDate().getYear();

        // 3. Perform the database updates. In a larger application, this should be a transaction.
        // For this project, sequential execution is acceptable.

        // Update 1: Change the leave status to APPROVED
        leaveDao.updateLeaveStatus(leaveId, "APPROVED", approverId);

        // Update 2: Increment the number of leave days taken in the balance table
        leaveBalanceDao.incrementLeaveTaken(leave.getUserId(), yearOfLeave, (int) durationInDays);

    }

    public void rejectLeave(int leaveId, int approverId) {
        leaveDao.updateLeaveStatus(leaveId, "REJECTED", approverId);
    }

    public List<Leave> getLeavesByUserId(int userId) throws SQLException {
        return leaveDao.getLeavesByUserId(userId);
    }

    // New method to fetch leaves by a date range
    public List<Leave> getLeavesByDateRange(int userId, LocalDate startDate, LocalDate endDate) throws SQLException {
        return leaveDao.getLeavesByDateRange(userId, startDate, endDate);
    }
    // New method to count pending leaves
    public int getPendingLeavesCount(int userId) throws SQLException {
        return leaveDao.getPendingLeavesCount(userId);
    }



}
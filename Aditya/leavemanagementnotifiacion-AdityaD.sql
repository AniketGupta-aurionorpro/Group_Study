DROP DATABASE IF EXISTS leavemanagementnotificationsection;
CREATE DATABASE leavemanagementnotificationsection;
USE leavemanagementnotificationsection;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    role ENUM('ADMIN', 'MANAGER', 'EMPLOYEE') NOT NULL,
    manager_id INT,
    salary DECIMAL(10, 2),
    joined_date DATE,
    FOREIGN KEY (manager_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE leaves (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    start_date DATE,
    end_date DATE,
    reason TEXT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    applied_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approved_by INT,
    approved_on TIMESTAMP,
    rejection_reason TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (approved_by) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    date DATE,
    status ENUM('PRESENT', 'ABSENT', 'LEAVE') NOT NULL,
    marked_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE leave_balance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    year INT,
    total_yearly_leave INT DEFAULT 24,
    yearly_leave_taken INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    receiver_id INT,
    message TEXT,
    seen BOOLEAN DEFAULT FALSE,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Sample Users
INSERT INTO users (id, name, email, password, role, manager_id, salary, joined_date) VALUES
(1, 'Admin User', 'admin@test.com', 'password', 'ADMIN', NULL, 75000.00, '2020-01-01'),
(2, 'Manager One', 'manager1@test.com', 'password', 'MANAGER', 1, 55000.00, '2021-02-01'),
(3, 'Employee A', 'employeeA@test.com', 'password', 'EMPLOYEE', 2, 30000.00, '2022-05-01'),
(4, 'Employee B', 'employeeB@test.com', 'password', 'EMPLOYEE', 2, 28000.00, '2023-03-15');

-- Leave Balance
INSERT INTO leave_balance (user_id, year, total_yearly_leave, yearly_leave_taken) VALUES
(3, YEAR(CURDATE()), 24, 0),
(4, YEAR(CURDATE()), 24, 0);

-- Leaves
INSERT INTO leaves (user_id, start_date, end_date, reason, status) VALUES
(3, '2025-09-08', '2025-09-09', 'Medical appointment', 'PENDING'),
(4, '2025-09-10', '2025-09-12', 'Family function', 'PENDING'),
(3, '2025-09-15', '2025-09-16', 'Urgent work at home', 'PENDING');

-- Attendance
INSERT INTO attendance (user_id, date, status) VALUES
(2, '2025-09-01', 'PRESENT'),
(2, '2025-09-02', 'LEAVE'),
(2, '2025-09-03', 'PRESENT'),
(3, '2025-09-01', 'PRESENT'),
(4, '2025-09-01', 'ABSENT');

-- Notifications
INSERT INTO notifications (receiver_id, message, seen) VALUES
(2, 'Employee A has requested leave from 2025-09-08 to 2025-09-09.', FALSE),
(2, 'Employee B has requested leave from 2025-09-10 to 2025-09-12.', FALSE),
(2, 'Employee A has requested leave from 2025-09-15 to 2025-09-16.', FALSE);


select * from  notifications;
select * from attendance;
select * from  leaves;
select * from leave_balance;
select * from users;
DROP DATABASE IF EXISTS leavemanagementnotificationsection;
CREATE DATABASE leavemanagementnotificationsection;
USE leavemanagementnotificationsection;

-- Users Table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    role ENUM('ADMIN', 'MANAGER', 'EMPLOYEE') NOT NULL,
    manager_id INT,
    salary DECIMAL(10,2),
    joined_date DATE,
    FOREIGN KEY(manager_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Leaves Table
CREATE TABLE leaves (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    start_date DATE,
    end_date DATE,
    reason TEXT,
    status ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
    applied_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approved_by INT,
    approved_on TIMESTAMP,
    rejection_reason TEXT,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(approved_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Leave Balance
CREATE TABLE leave_balance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    year INT,
    total_yearly_leave INT DEFAULT 24,
    yearly_leave_taken INT DEFAULT 0,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Notifications Table
CREATE TABLE notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    receiver_id INT,
    message TEXT,
    seen BOOLEAN DEFAULT FALSE,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Admin
INSERT INTO users (id, name, email, password, role, manager_id, salary, joined_date) VALUES
(1, 'Admin Name', 'admin@test.com', 'password', 'ADMIN', NULL, 90000.00, '2020-01-01');

-- Managers
INSERT INTO users (id, name, email, password, role, manager_id, salary, joined_date) VALUES
(2, 'Michael Manager', 'michael.manager@example.com', 'password', 'MANAGER', 1, 80000.00, '2021-03-15'),
(3, 'Mina Manager', 'mina.manager@example.com', 'password', 'MANAGER', 1, 82000.00, '2021-06-20');

-- Employees under Michael Manager (id = 2)
INSERT INTO users (name, email, password, role, manager_id, salary, joined_date) VALUES
('Ethan Employee', 'ethan.employee@example.com', 'password', 'EMPLOYEE', 2, 50000.00, '2022-02-01'),
('Emily Employee', 'emily.employee@example.com', 'password', 'EMPLOYEE', 2, 52000.00, '2022-05-12'),
('Ella Employee', 'ella.employee@example.com', 'password', 'EMPLOYEE', 2, 48000.00, '2022-03-10'),
('Eric Employee', 'eric.employee@example.com', 'password', 'EMPLOYEE', 2, 51000.00, '2022-07-18'),
('Evan Employee', 'evan.employee@example.com', 'password', 'EMPLOYEE', 2, 49500.00, '2022-04-05'),
('Eliza Employee', 'eliza.employee@example.com', 'password', 'EMPLOYEE', 2, 50500.00, '2022-06-21');

-- Employees under Mina Manager (id = 3)
INSERT INTO users (name, email, password, role, manager_id, salary, joined_date) VALUES
('Alex Employee', 'alex.employee@example.com', 'password', 'EMPLOYEE', 3, 51000.00, '2022-01-15'),
('Alice Employee', 'alice.employee@example.com', 'password', 'EMPLOYEE', 3, 52000.00, '2022-02-20'),
('Aaron Employee', 'aaron.employee@example.com', 'password', 'EMPLOYEE', 3, 50000.00, '2022-03-05'),
('Ava Employee', 'ava.employee@example.com', 'password', 'EMPLOYEE', 3, 53000.00, '2022-04-12'),
('Adam Employee', 'adam.employee@example.com', 'password', 'EMPLOYEE', 3, 49500.00, '2022-05-10'),
('Amber Employee', 'amber.employee@example.com', 'password', 'EMPLOYEE', 3, 50500.00, '2022-06-25');

-- Leave Balances (current year)
INSERT INTO leave_balance (user_id, year, total_yearly_leave, yearly_leave_taken) VALUES
(4,YEAR(CURDATE()),24,0),(5,YEAR(CURDATE()),24,0),(6,YEAR(CURDATE()),24,0),
(7,YEAR(CURDATE()),24,0),(8,YEAR(CURDATE()),24,0),(9,YEAR(CURDATE()),24,0),
(10,YEAR(CURDATE()),24,0),(11,YEAR(CURDATE()),24,0),(12,YEAR(CURDATE()),24,0),
(13,YEAR(CURDATE()),24,0),(14,YEAR(CURDATE()),24,0),(15,YEAR(CURDATE()),24,0);

-- Sample Leaves for Employees
INSERT INTO leaves (user_id, start_date, end_date, reason, status) VALUES
(4,'2025-09-08','2025-09-09','Medical appointment','PENDING'),
(5,'2025-09-10','2025-09-12','Family function','PENDING'),
(6,'2025-09-15','2025-09-16','Urgent work','PENDING'),
(7,'2025-09-09','2025-09-11','Personal work','PENDING'),
(8,'2025-09-12','2025-09-14','Vacation','PENDING'),
(9,'2025-09-18','2025-09-19','Conference','PENDING'),
(10,'2025-09-08','2025-09-09','Medical appointment','PENDING'),
(11,'2025-09-10','2025-09-12','Family function','PENDING'),
(12,'2025-09-15','2025-09-16','Urgent work','PENDING'),
(13,'2025-09-09','2025-09-11','Personal work','PENDING'),
(14,'2025-09-12','2025-09-14','Vacation','PENDING'),
(15,'2025-09-18','2025-09-19','Conference','PENDING');

-- Sample Notifications (optional initial)
INSERT INTO notifications (receiver_id, message, seen) VALUES
(2,'Ethan Employee requested leave from 2025-09-08 to 2025-09-09',FALSE),
(2,'Emily Employee requested leave from 2025-09-10 to 2025-09-12',FALSE),
(3,'Alex Employee requested leave from 2025-09-08 to 2025-09-09',FALSE),
(3,'Alice Employee requested leave from 2025-09-10 to 2025-09-12',FALSE);



select * from users;
select * from notifications;
select * from leaves;
select * from leave_balance ;
 
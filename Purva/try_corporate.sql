-- PRActise question in gs
DROP DATABASE TRY_CORPORATE;
CREATE DATABASE TRY_CORPORATE;

-- Use the new database
USE TRY_CORPORATE;

-- 1. Create DEPARTMENTS table
CREATE TABLE DEPARTMENTS (
    DEPT_ID     INT NOT NULL,
    DEPT_NAME   VARCHAR(50),
    LOCATION    VARCHAR(50),
    CONSTRAINT DEPT_PK PRIMARY KEY (DEPT_ID)
);
DROP TABLE EMPLOYEES;
-- 2. Create EMPLOYEES table
CREATE TABLE EMPLOYEES (
    EMP_ID      INT NOT NULL,
    EMP_NAME    VARCHAR(50),
    POSITION    VARCHAR(30),
    MANAGER_ID  INT,
    HIRE_DATE   DATETIME,
    SALARY      DECIMAL(10,2),
    BONUS       DECIMAL(10,2),
    DEPT_ID     INT NOT NULL,
    CONSTRAINT EMP_PK PRIMARY KEY (EMP_ID),
    CONSTRAINT EMP_DEPT_FK FOREIGN KEY (DEPT_ID) REFERENCES DEPARTMENTS(DEPT_ID)
);

-- 3. Create REGIONS table
CREATE TABLE REGIONS (
    REGION_ID   INT NOT NULL,
    REGION_NAME VARCHAR(50),
    CONSTRAINT REGION_PK PRIMARY KEY (REGION_ID)
);

-- 4. Create COUNTRIES table
CREATE TABLE COUNTRIES (
    COUNTRY_CODE CHAR(2) NOT NULL,
    COUNTRY_NAME VARCHAR(50),
    REGION_ID    INT,
    CONSTRAINT COUNTRY_PK PRIMARY KEY (COUNTRY_CODE),
    CONSTRAINT COUNTRY_REGION_FK FOREIGN KEY (REGION_ID) REFERENCES REGIONS(REGION_ID)
);

-- 5. Create LOCATIONS table
CREATE TABLE LOCATIONS (
    LOCATION_ID     INT NOT NULL,
    STREET_ADDRESS  VARCHAR(100),
    POSTAL_CODE     VARCHAR(15),
    CITY            VARCHAR(50) NOT NULL,
    STATE_PROVINCE  VARCHAR(50),
    COUNTRY_CODE    CHAR(2),
    CONSTRAINT LOCATION_PK PRIMARY KEY (LOCATION_ID),
    CONSTRAINT LOCATION_COUNTRY_FK FOREIGN KEY (COUNTRY_CODE) REFERENCES COUNTRIES(COUNTRY_CODE)
);

INSERT INTO DEPARTMENTS VALUES (101, 'HUMAN RESOURCES', 'NEW YORK');
INSERT INTO DEPARTMENTS VALUES (102, 'IT', 'SAN FRANCISCO');
INSERT INTO DEPARTMENTS VALUES (103, 'MARKETING', 'CHICAGO');
INSERT INTO DEPARTMENTS VALUES (104, 'FINANCE', 'DALLAS');

INSERT INTO EMPLOYEES VALUES (1001, 'ALICE JOHNSON', 'HR MANAGER', NULL, '2015-03-15', 75000.00, NULL, 101);
INSERT INTO EMPLOYEES VALUES (1002, 'BOB SMITH', 'HR ASSISTANT', 1001, '2017-07-20', 45000.00, 2000.00, 101);
INSERT INTO EMPLOYEES VALUES (1003, 'CAROL WHITE', 'IT MANAGER', NULL, '2014-05-10', 90000.00, NULL, 102);
INSERT INTO EMPLOYEES VALUES (1004, 'DAN BROWN', 'SOFTWARE ENGINEER', 1003, '2018-01-25', 65000.00, 5000.00, 102);
INSERT INTO EMPLOYEES VALUES (1005, 'EMILY DAVIS', 'MARKETING MANAGER', NULL, '2016-11-30', 80000.00, NULL, 103);
INSERT INTO EMPLOYEES VALUES (1006, 'FRANK MOORE', 'MARKETING EXEC', 1005, '2019-04-12', 55000.00, 3000.00, 103);
INSERT INTO EMPLOYEES VALUES (1007, 'GEORGE CLARK', 'FINANCE MANAGER', NULL, '2013-06-18', 95000.00, NULL, 104);
INSERT INTO EMPLOYEES VALUES (1008, 'HELEN SCOTT', 'FINANCIAL ANALYST', 1007, '2020-10-10', 60000.00, 2500.00, 104);

INSERT INTO REGIONS VALUES (1, 'North America');
INSERT INTO REGIONS VALUES (2, 'Europe');
INSERT INTO REGIONS VALUES (3, 'Asia');
INSERT INTO REGIONS VALUES (4, 'Africa');

INSERT INTO COUNTRIES VALUES ('US', 'United States', 1);
INSERT INTO COUNTRIES VALUES ('UK', 'United Kingdom', 2);
INSERT INTO COUNTRIES VALUES ('IN', 'India', 3);
INSERT INTO COUNTRIES VALUES ('ZA', 'South Africa', 4);
INSERT INTO COUNTRIES VALUES ('CA', 'Canada', 1);
INSERT INTO COUNTRIES VALUES ('DE', 'Germany', 2);

INSERT INTO LOCATIONS VALUES (2000, '1234 Main Street', '10001', 'New York', 'NY', 'US');
INSERT INTO LOCATIONS VALUES (2001, '987 Market Ave', '94103', 'San Francisco', 'CA', 'US');
INSERT INTO LOCATIONS VALUES (2002, '56 King St', 'M5H 1J9', 'Toronto', 'ON', 'CA');
INSERT INTO LOCATIONS VALUES (2003, '1 Queen Rd', 'WC2N 5DU', 'London', NULL, 'UK');
INSERT INTO LOCATIONS VALUES (2004, '99 MG Road', '560001', 'Bangalore', 'Karnataka', 'IN');
INSERT INTO LOCATIONS VALUES (2005, '12 Kaiserstrasse', '60313', 'Frankfurt', NULL, 'DE');
INSERT INTO LOCATIONS VALUES (2006, '77 Nelson Mandela Blvd', '8001', 'Cape Town', 'Western Cape', 'ZA');

-- List all departments
SELECT * FROM DEPARTMENTS;

-- Show all employees with their names and salaries
SELECT EMP_NAME, SALARY FROM EMPLOYEES;

-- Find the names of employees who work in the IT department
SELECT E.EMP_NAME, D.DEPT_NAME
FROM EMPLOYEES E
JOIN DEPARTMENTS D ON E.DEPT_ID = D.DEPT_ID
WHERE D.DEPT_NAME = 'IT';

-- Display all countries in the 'Asia' region
SELECT C.*
FROM COUNTRIES C
JOIN REGIONS R ON C.REGION_ID = R.REGION_ID
WHERE R.REGION_NAME = 'Asia';

-- Show employees hired after January 1, 2018
SELECT *
FROM EMPLOYEES
WHERE HIRE_DATE > '2018-01-01';

-- List all distinct job positions
SELECT DISTINCT POSITION
FROM EMPLOYEES;

-- Find the employee(s) with the highest salary
SELECT *
FROM EMPLOYEES
WHERE SALARY = (SELECT MAX(SALARY) FROM EMPLOYEES);

-- Get the number of employees in each department
SELECT D.DEPT_NAME, COUNT(E.EMP_ID) AS NUM_EMPLOYEES
FROM DEPARTMENTS D
LEFT JOIN EMPLOYEES E ON D.DEPT_ID = E.DEPT_ID
GROUP BY D.DEPT_NAME;


-- List cities where the company has offices
SELECT DISTINCT CITY
FROM LOCATIONS;

-- Find employees who do not receive a bonus
SELECT *
FROM EMPLOYEES
WHERE BONUS IS NULL OR BONUS = 0;

-- List each department's name with the total salary expense
SELECT D.DEPT_NAME, SUM(E.SALARY) AS TOTAL_SALARY
FROM DEPARTMENTS D
JOIN EMPLOYEES E ON D.DEPT_ID = E.DEPT_ID
GROUP BY D.DEPT_NAME;

-- Find the average salary for each job position
SELECT POSITION, AVG(SALARY) AS AVG_SALARY
FROM EMPLOYEES
GROUP BY POSITION;

-- List employees along with their manager's name
SELECT E.EMP_NAME AS EMPLOYEE, M.EMP_NAME AS MANAGER
FROM EMPLOYEES E
LEFT JOIN EMPLOYEES M ON E.MANAGER_ID = M.EMP_ID;

-- Find departments that have more than one employee
SELECT D.DEPT_NAME, COUNT(E.EMP_ID) AS NUM_EMPLOYEES
FROM DEPARTMENTS D
JOIN EMPLOYEES E ON D.DEPT_ID = E.DEPT_ID
GROUP BY D.DEPT_NAME
HAVING COUNT(E.EMP_ID) > 1;

-- Show all countries along with their region names
SELECT C.COUNTRY_NAME, R.REGION_NAME
FROM COUNTRIES C
JOIN REGIONS R ON C.REGION_ID = R.REGION_ID;

-- List locations with country and region names
SELECT L.LOCATION_ID, L.CITY, L.STATE_PROVINCE, C.COUNTRY_NAME, R.REGION_NAME
FROM LOCATIONS L
JOIN COUNTRIES C ON L.COUNTRY_CODE = C.COUNTRY_CODE
JOIN REGIONS R ON C.REGION_ID = R.REGION_ID;

-- Find departments where the average salary is above 70,000
SELECT D.DEPT_NAME, AVG(E.SALARY) AS AVG_SALARY
FROM DEPARTMENTS D
JOIN EMPLOYEES E ON D.DEPT_ID = E.DEPT_ID
GROUP BY D.DEPT_NAME
HAVING AVG(E.SALARY) > 70000;
-- seLF JOIN cause mANGer Also A emPLOYEE, so COONNECTIng ROWS wiTH Same COLIMN 
-- List employees who earn more than their managers
SELECT E.EMP_NAME AS EMPLOYEE, E.SALARY AS EMP_SALARY, 
       M.EMP_NAME AS MANAGER, M.SALARY AS MANAGER_SALARY
FROM EMPLOYEES E
-- -- For each E (employee), find M (manager) whose EMP_ID matches the MANAGER_ID of the employee.
JOIN EMPLOYEES M ON E.MANAGER_ID = M.EMP_ID   
WHERE E.SALARY > M.SALARY;

-- Find the second highest salary among employees
SELECT MAX(SALARY) AS SECOND_HIGHEST_SALARY
FROM EMPLOYEES
WHERE SALARY < (SELECT MAX(SALARY) FROM EMPLOYEES);


-- List employees with their department name and location
SELECT E.EMP_NAME, D.DEPT_NAME, D.LOCATION
FROM EMPLOYEES E
JOIN DEPARTMENTS D ON E.DEPT_ID = D.DEPT_ID;




-- NOTE

-- SET Definition:
-- Set operations combine the results of two or more SELECT statements into a single result set.
-- Why used:
-- When you need to merge rows vertically, or find differences or overlaps.



-- UNION
-- List all unique city names from LOCATIONS and department locations.

SELECT CITY AS LOCATION_NAME FROM LOCATIONS
UNION
SELECT LOCATION AS LOCATION_NAME FROM DEPARTMENTS;

-- UNION ALL. [Same as above but keep duplicates.]
SELECT CITY AS LOCATION_NAME FROM LOCATIONS
UNION ALL
SELECT LOCATION AS LOCATION_NAME FROM DEPARTMENTS;


-- INTERSECT (simulate)
-- Cities that are both in LOCATIONS and used as DEPARTMENTS locations.
SELECT DISTINCT LOCATION
FROM DEPARTMENTS
WHERE LOCATION IN (SELECT CITY FROM LOCATIONS);

-- EXCEPT (simulate)
-- Department locations that are not cities in LOCATIONS.
SELECT DISTINCT LOCATION
FROM DEPARTMENTS
WHERE LOCATION NOT IN (SELECT CITY FROM LOCATIONS);



-- What is an INDEX?

-- Definition:
-- An index is a data structure that improves the speed of read operations (SELECT) 
-- on a table at the cost of additional space and slower writes (INSERT, UPDATE, DELETE).
-- Why used:
-- To quickly locate rows without scanning the entire table.
-- Types:
-- Single-column index: On one column.
-- Composite index: On multiple columns.
-- Unique index: Enforces uniqueness.
-- Fulltext index: For searching text content (MySQL only, for CHAR, VARCHAR, TEXT).


-- ex Single Column Index
-- MySQL scans the entire table row by row
SELECT * FROM EMPLOYEES WHERE POSITION = 'HR MANAGER';

CREATE INDEX idx_position ON EMPLOYEES (POSITION);
SELECT * FROM EMPLOYEES WHERE POSITION LIKE 'HR MANAGER';

-- ex Composite Index (Multi-Column Index)
-- Create index on DEPT_ID and SALARY
CREATE INDEX idx_dept_salary ON EMPLOYEES (DEPT_ID, SALARY);
-- Find employees in IT department with salary above 70K
SELECT * FROM EMPLOYEES WHERE DEPT_ID = 102 AND SALARY > 70000;

CREATE INDEX idx_salary ON EMPLOYEES (DEPT_ID, SALARY);
-- Find employees in IT department with salary above 70K
SELECT * FROM EMPLOYEES WHERE DEPT_ID = 102 AND SALARY > 70000;


-- WHERE DEPT_ID = ... [y]
-- WHERE DEPT_ID = ... AND SALARY = ... [y]
-- But not WHERE SALARY = ... alone [n]


-- UNIQUE Index
-- Purpose: Ensure column values are unique — no duplicates.

CREATE UNIQUE INDEX idx_unique_emp_name ON EMPLOYEES (EMP_NAME);

-- Try inserting a duplicate 
INSERT INTO EMPLOYEES VALUES (2000, 'ALICE JOHNSON', 'HR INTERN', NULL, '2023-05-05', 40000, 0, 101);

-- DROP INDEX
-- Purpose: Remove an existing index if it’s not needed anymo
-- Drop the index we created
SHOW INDEXES FROM EMPLOYEES;

DROP INDEX idx_salary ON EMPLOYEES;



-- trigger

-- A Trigger is SQL code that automatically runs (fires) when a specific event happens on a table:

-- INSERT UPDATE DELETE You do not call it manually — the database does!
-- Types of Triggers BEFORE INSERT
-- AFTER INSERT
-- BEFORE UPDATE
-- AFTER UPDATE
-- BEFORE DELETE
-- AFTER DELETE

-- Key things
-- OLD = the row before change (only for UPDATE/DELETE)
-- NEW = the row after change (only for INSERT/UPDATE)

-- Suppose you want to log every new hire into an EMP_AUDIT table.
-- Make an audit table
DROP TABLE IF EXISTS EMPLOYEES;
DROP TABLE IF EXISTS EMP_AUDIT;
CREATE TABLE EMPLOYEES (
  EMP_ID INT PRIMARY KEY,
  EMP_NAME VARCHAR(50),
  SALARY DECIMAL(10,2)
);
-- Create EMP_AUDIT table
-- the audit table that keeps history of changes OLD_SALARY,NEW SAK ETC
-- AUTO_INCREMENT — this means each audit record is unique.
-- insert multiple audit rows for the same employee.
--  never get duplicate key errors 
CREATE TABLE EMP_AUDIT (
  AUDIT_ID INT AUTO_INCREMENT PRIMARY KEY,
  EMP_ID INT,
  EMP_NAME VARCHAR(50),
  ACTION_DATE DATETIME,
  ACTION_TYPE VARCHAR(20),
  OLD_SALARY DECIMAL(10,2),
  NEW_SALARY DECIMAL(10,2)
);
INSERT INTO EMPLOYEES (EMP_ID, EMP_NAME, SALARY)
VALUES (1, 'POO', 50000);

-- Create AFTER INSERT trigger
DELIMITER //

CREATE TRIGGER trg_after_insert_employee
AFTER INSERT ON EMPLOYEES
FOR EACH ROW
BEGIN
  INSERT INTO EMP_AUDIT (EMP_ID, EMP_NAME, ACTION_DATE, ACTION_TYPE, NEW_SALARY)
  VALUES (NEW.EMP_ID, NEW.EMP_NAME, NOW(), 'INSERT', NEW.SALARY);
END;
//

DELIMITER ;

INSERT INTO EMPLOYEES (EMP_ID, EMP_NAME, SALARY)
VALUES (2, 'BOB', 60000);

SELECT * FROM EMP_AUDIT;


-- Create AFTER UPDATE trigger
DELIMITER //

CREATE TRIGGER trg_after_update_employee
AFTER UPDATE ON EMPLOYEES
FOR EACH ROW
BEGIN
  INSERT INTO EMP_AUDIT (EMP_ID, EMP_NAME, ACTION_DATE, ACTION_TYPE, OLD_SALARY, NEW_SALARY)
  VALUES (NEW.EMP_ID, NEW.EMP_NAME, NOW(), 'UPDATE', OLD.SALARY, NEW.SALARY);
END;
//

DELIMITER ;

UPDATE EMPLOYEES
SET SALARY = SALARY + 5000
WHERE EMP_ID = 2;

SELECT * FROM EMP_AUDIT;

-- TO SEE TRIGGERS
SHOW TRIGGERS;
SHOW CREATE TRIGGER trg_after_insert_employee;
SHOW CREATE TRIGGER trg_after_update_employee;




-- NOTE

-- CTE = Common Table Expression

-- It’s a temporary named result set — like a temporary view — that you define at the start of a query and then use in the same query.
-- A temporary table that only exists for that query
-- Why use CTEs?
-- To break big queries into smaller, readable parts.
-- To reuse logic inside the same query.
-- To replace subqueries for better clarity.
-- Useful for recursive problems (like org chart hierarchies).

-- WITH [If you needed that avg_salary again (like in SELECT columns or JOINs), you’d repeat the whole subquery.]
WITH AvgSalary AS (
  SELECT AVG(SALARY) AS avg_salary FROM EMPLOYEES
)
SELECT * FROM EMPLOYEES WHERE SALARY > (SELECT avg_salary FROM AvgSalary);

-- WITHOUT
SELECT * 
FROM EMPLOYEES 
WHERE SALARY > (
  SELECT AVG(SALARY) 
  FROM EMPLOYEES
);






-- CTE to calculate total salary per department
WITH DeptSalary AS (
  SELECT DEPT_ID, SUM(SALARY) AS total_salary
  FROM EMPLOYEES
  GROUP BY DEPT_ID
)
SELECT D.DEPT_NAME, DS.total_salary
FROM DeptSalary DS
JOIN DEPARTMENTS D ON DS.DEPT_ID = D.DEPT_ID;

DESCRIBE EMPLOYEES;

ALTER TABLE EMPLOYEES ADD COLUMN DEPT_ID INT;

-- Now, add valid department IDs for your employees:
UPDATE EMPLOYEES SET DEPT_ID = 101 WHERE EMP_ID = 1011;
-- and so on, for other rows.

UPDATE EMPLOYEES SET DEPT_ID = 102 WHERE EMP_ID = 1003;
UPDATE EMPLOYEES SET DEPT_ID = 103 WHERE EMP_ID = 1005;
UPDATE EMPLOYEES SET DEPT_ID = 104 WHERE EMP_ID = 1007;

SELECT EMP_ID, EMP_NAME, DEPT_ID FROM EMPLOYEES;

-- checking just


SELECT EMP_ID, EMP_NAME FROM EMPLOYEES;

SELECT DEPT_ID, SUM(SALARY)
FROM EMPLOYEES
GROUP BY DEPT_ID;


-- WINDOW

-- Window Functions = SQL functions that perform a calculation across a set of rows related to the current row.
-- They do not collapse rows like GROUP BY — instead, they keep the original rows and add extra info (like ranks, running totals, averages, etc.)
-- Type	              Function	         What it does
-- Ranking	          ROW_NUMBER()	     Gives a unique number to each row in order
-- Ranking	          RANK()	         Gives same rank for ties, skips next rank
-- Ranking	          DENSE_RANK()	     Gives same rank for ties, no gaps
-- Aggregate	      SUM() OVER	     Running total
-- Aggregate	      AVG() OVER	     Moving average
-- Value	          LEAD(), LAG()	     Look ahead / behind


-- Rank employees by salary within their department
-- PARTITION BY DEPT_ID → Restart rank for each department.
SELECT
  EMP_ID,
  EMP_NAME,
  DEPT_ID,
  SALARY,
  RANK() OVER (PARTITION BY DEPT_ID ORDER BY SALARY DESC) AS SALARY_RANK
FROM EMPLOYEES;

-- Running total salary in entire company (no partition)
SELECT
  EMP_ID,
  EMP_NAME,
  SALARY,
  SUM(SALARY) OVER (ORDER BY HIRE_DATE) AS RUNNING_TOTAL
FROM EMPLOYEES;

-- Find employee’s salary vs department average
-- PARTITION BY DEPT_ID → Calculate average salary per department.
SELECT
  EMP_ID,
  EMP_NAME,
  DEPT_ID,
  SALARY,
  AVG(SALARY) OVER (PARTITION BY DEPT_ID) AS DEPT_AVG_SALARY
FROM EMPLOYEES;

-- Lead — see next higher paid person
SELECT
  EMP_NAME,
  SALARY,
  LEAD(EMP_NAME) OVER (ORDER BY SALARY DESC) AS NEXT_HIGHER_NAME,
  LEAD(SALARY) OVER (ORDER BY SALARY DESC) AS NEXT_HIGHER_SALARY
FROM EMPLOYEES;

-- Dense rank for tie handling
-- DENSE_RANK → Same salary → same rank → NO gaps in ranking.
SELECT
  EMP_NAME,
  SALARY,
  DENSE_RANK() OVER (ORDER BY SALARY DESC) AS DENSE_RANK_SALARY
FROM EMPLOYEES;

-- Check table
SELECT * FROM EMPLOYEES;

-- Try first window query
SELECT EMP_ID, EMP_NAME, SALARY,
RANK() OVER (ORDER BY SALARY DESC) AS RANK_ORDER
FROM EMPLOYEES;


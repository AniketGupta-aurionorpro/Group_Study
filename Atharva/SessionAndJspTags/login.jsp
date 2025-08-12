<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h2>Student Login</h2>
    <form action="welcome.jsp" method="post">
        Student Name: <input type="text" name="studentName" placeholder="Name" required><br><br>
        Student ID: <input type="text" name="studentId" placeholder="student ID" required><br><br>
        <input type="submit" value="Login">
    </form>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%
    session.invalidate(); // end the session
%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h2>You have been logged out.</h2>
    <a href="login.jsp">Login Again</a>
</body>
</html>
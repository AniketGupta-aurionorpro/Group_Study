<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.util.*" %>
    

    <%@ include file="Header.jsp" %>
<!DOCTYPE html>
<%
    String studentName = request.getParameter("studentName");
    String studentId = request.getParameter("studentId");

    if (studentName != null && studentId != null) {
        // Store data in session
        session.setAttribute("studentName", studentName);
        session.setAttribute("studentId", studentId);
    } else {
        studentName = (String) session.getAttribute("studentName");
        studentId = (String) session.getAttribute("studentId");

        if (studentName == null || studentId == null) {
            response.sendRedirect("login.jsp");
            return;
        }
    }
%>

<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h2>Welcome, <%= studentName %>!</h2>
    <p>Your Student ID is: <%= studentId %></p>

    <!-- URL rewriting to include session ID -->
    <a href="logout.jsp?<%= response.encodeURL("") %>">Logout</a>
</body>
</html>
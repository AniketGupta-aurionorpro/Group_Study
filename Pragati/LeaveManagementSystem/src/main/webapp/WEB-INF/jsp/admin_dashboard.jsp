<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String userName = (String) session.getAttribute("userName");
    String role = (String) session.getAttribute("role");

    if (userName == null || !"ADMIN".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/admin.css">
</head>
<body>
<div class="sidebar">
    <h3>Welcome, <%= userName %></h3>
    <a href="<%= request.getContextPath() %>/admin/addEmployee" target="contentFrame">Add Employee/Manager</a>
    <a href="<%= request.getContextPath() %>/admin/editUser" target="contentFrame">Edit Employee/Manager</a>
    <a href="<%= request.getContextPath() %>/logout">Logout</a>
</div>

<div class="main">
    <iframe name="contentFrame" src="<%= request.getContextPath() %>/admin/welcome" width="100%" height="600px"></iframe>
</div>
</body>
</html>

<%@page import="java.util.List"%>
<%@ page session="true" %>
<%
    List<String> notifications = (List<String>) request.getAttribute("notifications");
    if(notifications == null){
        response.sendRedirect("managerDashboard.jsp");
        return;
    }
%>
<html>
<head>
<title>Notifications</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow-sm p-4">
        <h2 class="text-primary mb-4">Notifications</h2>
        <ul class="list-group">
        <% for(String n : notifications){ %>
            <li class="list-group-item list-group-item-info mb-2"><%=n%></li>
        <% } %>
        </ul>
        <a href="managerDashboard.jsp" class="btn btn-secondary mt-3">Back to Dashboard</a>
    </div>
</div>
</body>
</html>

 <%@ page import="com.notification.model.User" %>
<%@ page session="true" %>
<%
    User manager = (User) session.getAttribute("manager");
    if(manager == null){
        response.sendRedirect("index.jsp");
        return;
    }
%>
<html>
<head>
    <title>Manager Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow-sm p-4">
        <h2 class="mb-4 text-primary">Welcome, <%=manager.getName()%></h2>
        <div class="d-grid gap-2 col-6 mx-auto">
            <a href="managerNotifications.jsp" class="btn btn-success btn-lg">View Pending Leaves</a>
            <a href="NotificationServlet" class="btn btn-info btn-lg">View Notifications</a>
            <a href="index.jsp" class="btn btn-secondary btn-lg">Logout</a>
        </div>
    </div>
</div>
</body>
</html>
 
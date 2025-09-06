 <%@ page import="java.util.*" %>
<%
    List<String> notifications = (List<String>) request.getAttribute("notifications");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Notifications</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h3>Notifications</h3>
    <ul class="list-group">
        <% for(String n : notifications) { %>
        <li class="list-group-item"><%=n%></li>
        <% } %>
    </ul>
    <a href="index.jsp" class="btn btn-secondary mt-3">Back</a>
</div>
</body>
</html>
 
<%@page import="java.util.List"%>
<%@page import="com.notification.dao.LeaveDAO"%>
<%@page import="com.notification.model.Leave"%>
<%@page import="com.notification.model.User"%>
<%@ page session="true" %>
<%
    User manager = (User) session.getAttribute("manager");
    if(manager == null){
        response.sendRedirect("index.jsp");
        return;
    }
    String filterName = request.getParameter("filterName");
    String filterId = request.getParameter("filterId");
    LeaveDAO dao = new LeaveDAO();
    List<Leave> leaves = dao.getPendingLeavesForManager(manager.getId(), filterName, filterId);
%>
<html>
<head>
<title>Pending Leaves</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow-sm p-4">
        <h2 class="text-primary mb-4">Pending Leaves</h2>

        <!-- Filter Form -->
        <form class="row g-3 mb-4" method="get">
            <div class="col-md-4">
                <input type="text" class="form-control" name="filterName" placeholder="Employee Name" value="<%=filterName != null ? filterName : ""%>"/>
            </div>
            <div class="col-md-4">
                <input type="text" class="form-control" name="filterId" placeholder="Employee ID" value="<%=filterId != null ? filterId : ""%>"/>
            </div>
            <div class="col-md-4">
                <button type="submit" class="btn btn-primary">Filter</button>
            </div>
        </form>

        <!-- Leaves Table -->
        <table class="table table-hover table-bordered text-center align-middle">
            <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Reason</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
            <% for(Leave l : leaves) { %>
                <tr>
                    <td><%=l.getId()%></td>
                    <td><%=l.getEmployeeName()%></td>
                    <td><%=l.getStartDate()%></td>
                    <td><%=l.getEndDate()%></td>
                    <td><%=l.getReason()%></td>
                    <td>
                        <form method="post" action="LeaveRequestServlet" class="d-flex gap-2 justify-content-center">
                            <input type="hidden" name="leaveId" value="<%=l.getId()%>"/>
                            <input type="hidden" name="managerId" value="<%=manager.getId()%>"/>
                            <select class="form-select" name="action" required>
                                <option value="">Select Action</option>
                                <option value="APPROVED">Approve</option>
                                <option value="REJECTED">Reject</option>
                            </select>
                            <input type="text" class="form-control" name="rejectionReason" placeholder="Reason if reject"/>
                            <button type="submit" class="btn btn-success">Submit</button>
                        </form>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
        <a href="managerDashboard.jsp" class="btn btn-secondary mt-3">Back to Dashboard</a>
    </div>
</div>
</body>
</html>

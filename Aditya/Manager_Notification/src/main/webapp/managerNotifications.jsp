 <%@ page import="java.util.*,com.notification.dao.LeaveDAO,com.notification.model.Leave" %>
<%
    LeaveDAO dao = new LeaveDAO();
    List<Leave> leaves = dao.getPendingLeavesForManager(2);
    List<String> attendance = dao.getManagerAttendance(2);
%>
<!DOCTYPE html>
<html>
<head>
    <title>Pending Leave Requests</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h3>Pending Leave Requests</h3>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th><th>Employee</th><th>Dates</th><th>Reason</th><th>Action</th>
        </tr>
        </thead>
        <tbody>
        <% for(Leave l : leaves) { %>
        <tr>
            <td><%=l.getId()%></td>
            <td><%=l.getEmployeeName()%></td>
            <td><%=l.getStartDate()%> to <%=l.getEndDate()%></td>
            <td><%=l.getReason()%></td>
            <td>
                <form method="post" action="LeaveRequestServlet" style="display:inline;">
                    <input type="hidden" name="leaveId" value="<%=l.getId()%>">
                    <input type="hidden" name="action" value="approve">
                    <button class="btn btn-success btn-sm">Approve</button>
                </form>
                <form method="post" action="LeaveRequestServlet" style="display:inline;">
                    <input type="hidden" name="leaveId" value="<%=l.getId()%>">
                    <input type="hidden" name="action" value="reject">
                    <input type="text" name="rejectionReason" placeholder="Reason" required>
                    <button class="btn btn-danger btn-sm">Reject</button>
                </form>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>

    <h3 class="mt-5">My Attendance</h3>
    <ul class="list-group">
        <% for(String a : attendance) { %>
        <li class="list-group-item"><%=a%></li>
        <% } %>
    </ul>

    <a href="index.jsp" class="btn btn-secondary mt-3">Back</a>
</div>
</body>
</html>
 
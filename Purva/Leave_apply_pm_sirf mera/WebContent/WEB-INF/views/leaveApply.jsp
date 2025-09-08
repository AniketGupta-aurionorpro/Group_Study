<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.aurionpro.leave_management.model.User"%>
<%@ page import="com.aurionpro.leave_management.model.LeaveBalance"%>
<%@ page import="com.aurionpro.leave_management.model.Leave"%>
<%@ page import="java.util.List"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="java.util.Calendar"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Apply for Leave</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/leave.css">
<link rel="stylesheet" type="text/css" 
    href="<%=request.getContextPath()%>/css/leaveApply.css">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
		<div class="container-fluid">
			<a class="navbar-brand" href="<%= request.getContextPath() %>/ManagerDashboardController"> <i class="bi bi-building"></i>
				Leave Management System
			</a>

			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarNav"
				aria-controls="navbarNav" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>

			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav me-auto">
					<li class="nav-item"><a class="nav-link"
						href="<%=request.getContextPath()%>/manager/testLogin"><i
							class="bi bi-pencil-square"></i> Apply Leave</a></li>
					<li class="nav-item"><a class="nav-link"
						href="notifications.jsp"><i class="bi bi-bell"></i>
							Notifications</a></li>
					<li class="nav-item"><a class="nav-link" href="attendance.jsp"><i
							class="bi bi-calendar-check"></i> Attendance</a></li>
				</ul>
				<form class="d-flex">
					<button class="btn btn-light btn-sm" type="button"
						onclick="window.location.href='logout'">
						<i class="bi bi-box-arrow-right"></i> Logout
					</button>
				</form>
			</div>
		</div>
	</nav>
	
	<%
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	User currentUser = (User) request.getAttribute("currentUser");
	LeaveBalance leaveBalance = (LeaveBalance) request.getAttribute("leaveBalance");
	String adminName = (String) request.getAttribute("adminName");
	List<Leave> pastLeaves = (List<Leave>) request.getAttribute("pastLeaves");
	List<Leave> searchResults = (List<Leave>) request.getAttribute("searchResults");
	%>
	
    <script>
        const leaveData = [
            <%if (pastLeaves != null && !pastLeaves.isEmpty()) {%>
                <%for (int i = 0; i < pastLeaves.size(); i++) {
		Leave leave = pastLeaves.get(i);
		String rejectionReason = leave.getRejectionReason() != null
				? leave.getRejectionReason().replace("\\", "\\\\").replace("\"", "\\\"")
				: "";%>
                    {
                        startDate: '<%=formatter.format(leave.getStartDate())%>',
                        endDate: '<%=formatter.format(leave.getEndDate())%>',
                        status: '<%=leave.getStatus().toLowerCase()%>',
                        rejectionReason: "<%=rejectionReason%>"
                    }<%=i < pastLeaves.size() - 1 ? "," : ""%>
                <%}%>
            <%}%>
        ];
        const totalYearlyLeave = <%=leaveBalance != null ? leaveBalance.getTotalYearlyLeave() : 24%>;
        const yearlyLeaveTaken = <%=leaveBalance != null ? leaveBalance.getYearlyLeaveTaken() : 0%>;
    </script>
    
	<div class="container">
		<div class="left-panel">
			<%
			if (currentUser != null) {
			%>
			<div class="info-section">
				<h3 onclick="toggleContent(this)">Manager Details</h3>
				<div class="content">
					<p>
						<strong>Name:</strong>
						<%=currentUser.getName()%></p>
					<p>
						<strong>ID:</strong>
						<%=currentUser.getId()%></p>
					<p>
						<strong>Email:</strong>
						<%=currentUser.getEmail()%></p>
				</div>
			</div>
			<%
			}
			%>


			<%
			if (leaveBalance != null) {
			%>
			<div class="info-section">
				<h3 onclick="toggleContent(this)">Remaining Holidays</h3>
				<div class="content">
					<p>
						<strong>Remaining Holiday (Year):</strong>
						<%=24 - leaveBalance.getYearlyLeaveTaken()%></p>
				</div>
			</div>
			<%
			} else {
			%>
			<div class="info-section">
				<h3 onclick="toggleContent(this)">Remaining Holidays</h3>
				<div class="content">
					<p>
						<strong>Remaining Holiday (Year):</strong> 24
					</p>
				</div>
			</div>
			<%
			}
			%>
			<div class="info-section">
				<h3 onclick="toggleContent(this)">Leave Approver</h3>
				<div class="content">
					<p><%=adminName%></p>
				</div>
			</div>
			<div class="info-section">
				<h3 onclick="toggleContent(this)">Leave History</h3>
				<div class="content">
					<form action="<%=request.getContextPath()%>/manager/leave"
						method="post">
						<input type="hidden" name="action" value="search">
						<div class="form-group">
							<label for="searchStartDate">From Date:</label> <input
								type="date" id="searchStartDate" name="searchStartDate" required>
						</div>
						<div class="form-group">
							<label for="searchEndDate">To Date:</label> <input type="date"
								id="searchEndDate" name="searchEndDate" required>
						</div>
						<div class="button-container">
							<button type="submit">Search</button>
						</div>
					</form>
					<hr>
					<div id="leaveHistoryResults">
						<%
						if (searchResults != null) {
						%>
						<table class="leave-history-table">
							<thead>
								<tr>
									<th>Applied On</th>
									<th>From</th>
									<th>To</th>
									<th>Status</th>
									<th>Reason</th>
									<th>Rejection Reason</th>
								</tr>
							</thead>
							<tbody>
								<%
								if (searchResults.isEmpty()) {
								%>
								<tr>
									<td colspan="6" style="text-align: center;">No leave
										history found for this date range.</td>
								</tr>
								<%
								} else {
								%>
								<%
								for (Leave leave : searchResults) {
								%>
								<tr>
									<td><%=formatter.format(leave.getAppliedOn().toLocalDateTime().toLocalDate())%></td>
									<td><%=formatter.format(leave.getStartDate())%></td>
									<td><%=formatter.format(leave.getEndDate())%></td>
									<td><span
										class="status-<%=leave.getStatus().toLowerCase()%>"><%=leave.getStatus()%></span></td>
									<td><%=leave.getReason()%></td>
									<td>
										<%
										if ("REJECTED".equals(leave.getStatus())) {
										%> <%=leave.getRejectionReason() != null ? leave.getRejectionReason() : "N/A"%>
										<%
										} else {
										%> - <%
										}
										%>
									</td>
								</tr>
								<%
								}
								%>
								<%
								}
								%>
							</tbody>
						</table>
						<%
						} else {
						%>
						<p>Enter a date range and click search to view leave history.</p>
						<%
						}
						%>
					</div>
				</div>
			</div>
		</div>

		<div class="right-panel">
			<div class="calendar-section">
				<div class="calendar-header">
					<button onclick="changeMonth(-1)">&#9664; Prev</button>
					<h2 id="currentMonthYear"></h2>
					<button onclick="changeMonth(1)">Next &#9654;</button>
				</div>
				<div class="calendar-grid" id="calendarGrid">
					<div class="calendar-day day-name">Sun</div>
					<div class="calendar-day day-name">Mon</div>
					<div class="calendar-day day-name">Tue</div>
					<div class="calendar-day day-name">Wed</div>
					<div class="calendar-day day-name">Thu</div>
					<div class="calendar-day day-name">Fri</div>
					<div class="calendar-day day-name">Sat</div>
				</div>
			</div>

			<h3>Apply for Leave</h3>
			<%
			if (request.getAttribute("message") != null) {
			%>
			<div class="message success"><%=request.getAttribute("message")%></div>
			<%
			}
			%>
			<%
			if (request.getAttribute("error") != null) {
			%>
			<div class="message error"><%=request.getAttribute("error")%></div>
			<%
			}
			%>

			<form action="<%=request.getContextPath()%>/manager/leave"
				method="post">
				<div class="form-group">
					<label for="startDate">Start Date:</label> <input type="date"
						id="startDate" name="startDate" required>
				</div>
				<div class="form-group">
					<label for="endDate">End Date:</label> <input type="date"
						id="endDate" name="endDate" required>
				</div>
				<div class="form-group">
					<label for="reason">Description for Holiday:</label>
					<textarea id="reason" name="reason" rows="4" required></textarea>
				</div>
				<div class="button-container">
					<button type="submit">Apply for leave</button>
				</div>
			</form>
		</div>
	</div>

	<div class="overlay" id="overlay"></div>
	<div class="rejection-reason-popup" id="rejectionPopup">
		<h4>Rejection Reason</h4>
		<p id="rejectionText"></p>
		<button onclick="hidePopup()">Close</button>
	</div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/leaveApply.js"></script>
</body>
</html>
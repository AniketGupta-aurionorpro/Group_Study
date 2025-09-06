<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
	<title>Pending Requests - Leave Management</title>
	<link rel="stylesheet" type="text/css"
		href="<c:url value='/css/AdminDashboard.css' />">
	<link rel="stylesheet" type="text/css"
		href="<c:url value='/css/Request.css' />">
	<link 
	href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined"
	rel="stylesheet" />
</head>
<body>
	<div class="sidebar">
		<h2>Admin Dashboard</h2>
		<a href="${pageContext.request.contextPath}/admin?command=DASHBOARD"
			class="btn"><span class="material-symbols-outlined">
				calendar_month</span>Callender</a> 
		<a href="" class="btn"><span class="material-symbols-outlined">menu</span>Requests</a> 
		<a href="" class="btn"><span class="material-symbols-outlined">person_add</span>Add Employee</a> 
		<a href="" class="btn"><span class="material-symbols-outlined">groups</span>View Employee</a> 
		<a href="" class="btn"><span class="material-symbols-outlined">logout</span>Logout</a>
	</div>

	<div class="content">

		<!-- Requests Section -->
		<div id="requests-tab" class="tab-section">
			<h3>Pending Requests</h3>
			<c:choose>
				<c:when test="${empty pendingRequests}">
					<p>No pending requests.</p>
				</c:when>
				<c:otherwise>
					<c:forEach var="req" items="${pendingRequests}">
						<div class="request-card">
							<p>
								<b>${req.user.name}</b> requested leave from ${req.startDate} to
								${req.endDate}
							</p>
							<p>Reason: ${req.reason}</p>

							<!-- Display overlapping users -->
							<c:if test="${not empty approvedLeaves[req.id]}">
								<button class="collapsible"><b>${fn:length(approvedLeaves[req.id])} Employees on leave during this period:</b></button>
								<div class="collapsible-content">
									 <ul>
										<c:forEach var="u" items="${approvedLeaves[req.id]}">
											<li>${u.name}(${u.role}) - ${u.email}</li>
										</c:forEach>
									</ul>
								</div>
							</c:if>

							<form>
								<input type="hidden" name="leaveId" value="${req.id}">
								<button type="button" name="action"
									onclick="takeLeaveAction(${req.id}, 'APPROVE')"
									class="btn-approve">Approve</button>
								<button type="button" name="action"
									onclick="takeLeaveAction(${req.id}, 'REJECT')"
									class="btn-reject">Reject</button>
							</form>
						</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<script src="<c:url value='/js/Request.js'/>"></script>
</body>
</html>
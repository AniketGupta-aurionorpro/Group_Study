<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
<title>Admin Dashboard - Leave Management</title>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/css/AdminDashboard.css' />">
<link rel="stylesheet" type="text/css"
	href="<c:url value='/css/snackbar.css' />">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.11/index.global.min.css">
<link
	href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined"
	rel="stylesheet" />
<script
	src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.11/index.global.min.js"></script>
</head>
<body>
	<div class="sidebar">
		<h2>Admin Dashboard</h2>
		<a href="" class="btn"><span class="material-symbols-outlined">
				calendar_month</span>Calender</a> 
		<a href="${pageContext.request.contextPath}/admin?command=REQUESTS"
			class="btn"> <span class="material-symbols-outlined">menu</span>Requests
			<c:if test="${not empty pendingRequestCount}">
				<span class="badge">${pendingRequestCount}</span>
			</c:if></a> 
		<a href="" class="btn"><span class="material-symbols-outlined">person_add</span>Add Employee</a> 
		<a href="" class="btn"><span class="material-symbols-outlined">groups</span>View Employee</a> 
		<a href="" class="btn"><span class="material-symbols-outlined">logout</span>Logout</a>
	</div>

	<div class="content">
		<div class="dashboard-grid">

			<!-- Left Column -->
			<div class="left-col">
				<div id="calendar-section" class="tab-section">
					<h3>Calendar View</h3>
					<div id="calendar"></div>
				</div>

				<!-- Employees on leave -->
				<div id="leave-section" class="tab-section">
					<h3>Employees on Leave</h3>
					<div id="leave-list">
						<p>Select a date to view employees on leave.</p>
					</div>
				</div>
			</div>

			<!-- Right Column -->
			<div class="right-col">
				<!-- Holiday List Section -->
				<div id="notif-section" class="tab-section">
					<h3>Notifications</h3>
					<c:if test="${unseenNotificationCount > 0}">
						<p>You have ${unseenNotificationCount} new notification(s).</p>
					</c:if>
					<c:if test="${unseenNotificationCount == 0}">
						<p>You have no new notifications.</p>
					</c:if>
					<button class="btn-view" id="notifBtn">View Notifications</button>
				</div>

				<!-- Holiday List Section -->
				<div id="holiday-section" class="tab-section">
					<h3>Holidays</h3>
					<!-- Add holiday action (shown only on calendar click) -->
					<div id="holiday-action" style="margin-top: 10px; display: none;">
						<input type="text" id="holidayReason"
							placeholder="Enter holiday reason" Required />
						<button id="markHolidayBtn" class="btn-approve">Add
							Holiday</button>
					</div>
					<ul id="holiday-list"></ul>
				</div>

			</div>
		</div>
	</div>


	<!-- Notification Modal -->
	<div id="notifModal" class="modal">

		<!-- Modal content -->
		<div class="modal-content">
			<span class="close">&times;</span>
			<h3>Notifications</h3>

			<!-- Top Action Buttons -->
			<div class="notif-actions">
				<button id="markAllReadBtn" class="btn">Mark All as Read</button>
				<button id="deleteAllReadBtn" class="btn danger">Delete
					Read</button>
			</div>

			<!-- Notifications List -->
			<ul class="notif-list">
				<c:forEach var="n" items="${notifications}">
					<li class="notif-item ${n.seen ? 'read' : 'unread'}"
						data-id="${n.id}"><span class="notif-text">${n.message}</span>
						<span class="notif-time"> <c:choose>
								<c:when
									test="${fn:substring(n.created_on,0,10) == fn:substring(today,0,10)}">
									<fmt:formatDate value="${n.created_on}" pattern="HH:mm" />
								</c:when>
								<c:otherwise>
									<fmt:formatDate value="${n.created_on}" pattern="yyyy-MM-dd" />
								</c:otherwise>
							</c:choose>
							&nbsp;
					</span>
						<div class="notif-buttons">
							<c:if test="${!n.seen}">
								<button class="btn small mark-read-btn">Mark Read</button>
							</c:if>
							<button class="btn small danger delete-btn">Delete</button>
						</div></li>
				</c:forEach>
			</ul>
		</div>

	</div>
	
	<div id="snackbar">Updated Holidays</div>
	<script src="<c:url value='/js/AdminDashboard.js'/>"></script>
</body>
</html>
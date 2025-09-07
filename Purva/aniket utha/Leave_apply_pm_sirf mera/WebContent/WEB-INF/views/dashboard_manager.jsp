<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Manager Dashboard</title>
<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- Bootstrap Icons -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
	rel="stylesheet">

<style>
body {
	background-color: #f8f9fa;
}

.navbar-brand {
	font-weight: bold;
	font-size: 1.3rem;
}

.card {
	border-radius: 12px;
	box-shadow: 0px 3px 8px rgba(0, 0, 0, 0.1);
}

.table thead {
	background-color: #0d6efd;
	color: white;
}

.table tbody tr:hover {
	background-color: #f1f1f1;
}
</style>
</head>
<body>

	<!-- Navbar -->
	<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
		<div class="container-fluid">
			<!-- Logo -->
			<a class="navbar-brand" href="#"> <i class="bi bi-building"></i>
				Leave Management System
			</a>

			<!-- Mobile Toggle -->
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarNav"
				aria-controls="navbarNav" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>

			<!-- Links -->
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav me-auto">
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/manager/testLogin"><i
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

	<!-- Main Content -->
	<div class="container my-5">

		<!-- Dashboard Header -->
		<div class="mb-4">
			<h2 class="fw-bold text-primary">
				<i class="bi bi-speedometer2"></i> Manager Dashboard
			</h2>
			<p class="text-muted">Welcome! Here is the list of your
				employees.</p>
		</div>

		<!-- Alert for Messages -->
		<c:if test="${not empty message}">
			<div class="alert alert-warning">${message}</div>
		</c:if>

		<!-- Employees Section -->
		<div class="card p-4">
			<h4 class="mb-3">
				<i class="bi bi-people"></i> My Employees
			</h4>

			<c:if test="${not empty employees}">
				<div class="table-responsive">
					<table class="table table-hover table-bordered align-middle">
						<thead>
							<tr>
								<th>ID</th>
								<th><i class="bi bi-person"></i> Name</th>
								<th><i class="bi bi-envelope"></i> Email</th>
								<th><i class="bi bi-cash"></i> Salary</th>
								<th><i class="bi bi-calendar-event"></i> Joined Date</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="emp" items="${employees}">
								<tr>
									<td>${emp.id}</td>
									<td>${emp.name}</td>
									<td>${emp.email}</td>
									<td>₹ ${emp.salary}</td>
									<td>${emp.joinedDate}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>
		</div>
	</div>

	<!-- Bootstrap JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

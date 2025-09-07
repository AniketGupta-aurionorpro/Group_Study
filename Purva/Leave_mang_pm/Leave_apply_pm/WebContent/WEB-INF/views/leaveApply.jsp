<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.aurionpro.model.User"%>
<%@ page import="com.aurionpro.model.LeaveBalance"%>
<%@ page import="com.aurionpro.model.Leave"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Apply for Leave</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/leave.css">
<style>
.rejection-reason-popup {
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	background-color: white;
	padding: 20px;
	border: 1px solid #ccc;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
	z-index: 1000;
	display: none;
	max-width: 400px;
	text-align: center;
	border-radius: 8px;
}

.rejection-reason-popup button {
	margin-top: 10px;
	padding: 8px 16px;
	cursor: pointer;
	border: none;
	background-color: #007bff;
	color: white;
	border-radius: 4px;
}

.overlay {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.5);
	z-index: 999;
	display: none;
}
</style>
</head>
<body>
	<%
	User currentUser = (User) request.getAttribute("currentUser");
	LeaveBalance leaveBalance = (LeaveBalance) request.getAttribute("leaveBalance");
	String adminName = (String) request.getAttribute("adminName");
	List<Leave> pastLeaves = (List<Leave>) request.getAttribute("pastLeaves");
	List<Leave> searchResults = (List<Leave>) request.getAttribute("searchResults");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	%>
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
									<td><%=formatter.format(leave.getAppliedOn())%></td>
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

	<script>
        let currentDate = new Date();
        const daysContainer = document.getElementById('calendarGrid');
        const monthYearDisplay = document.getElementById('currentMonthYear');
        const startDateInput = document.getElementById('startDate');
        const endDateInput = document.getElementById('endDate');
        const WEEK_DAYS = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

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
        
        let startSelection = null;
        let endSelection = null;

        function renderCalendar() {
            daysContainer.innerHTML = '';
            
            WEEK_DAYS.forEach(day => {
                const dayNameDiv = document.createElement('div');
                dayNameDiv.classList.add('calendar-day', 'day-name');
                dayNameDiv.textContent = day;
                daysContainer.appendChild(dayNameDiv);
            });

            const year = currentDate.getFullYear();
            const month = currentDate.getMonth();
            const firstDayOfMonth = new Date(year, month, 1).getDay();
            const daysInMonth = new Date(year, month + 1, 0).getDate();

            monthYearDisplay.textContent = new Date(year, month).toLocaleString('default', { month: 'long', year: 'numeric' });

            for (let i = 0; i < firstDayOfMonth; i++) {
                const emptyDiv = document.createElement('div');
                emptyDiv.classList.add('calendar-day', 'disabled-day');
                daysContainer.appendChild(emptyDiv);
            }

            for (let i = 1; i <= daysInMonth; i++) {
                const dayDiv = document.createElement('div');
                dayDiv.classList.add('calendar-day');
                const dayDate = new Date(year, month, i);
                dayDiv.textContent = i;
                
                if (dayDate.getDay() === 0 || dayDate.getDay() === 6) {
                    dayDiv.classList.add('weekend', 'disabled-day');
                }
                
                const formattedDate = year + '-' + (month + 1).toString().padStart(2, '0') + '-' + i.toString().padStart(2, '0');
                dayDiv.dataset.date = formattedDate;

                const leaveForDay = leaveData.find(leave => {
                    const leaveStart = new Date(leave.startDate);
                    const leaveEnd = new Date(leave.endDate);
                    return isDateInRange(dayDate, leaveStart, leaveEnd);
                });
                
                if (leaveForDay) {
                    const status = leaveForDay.status;
                    dayDiv.classList.add(status);
                    //dayDiv.dataset.leaveStatus = status;
                    //dayDiv.dataset.rejectionReason = leaveForDay.rejectionReason;
                    if (status === 'rejected') {
                        //dayDiv.dataset.rejectionReason = leaveForDay.rejectionReason;
                        //dayDiv.title = `Status: Rejected. Reason: ${leaveForDay.rejectionReason}`;
                        dayDiv.title = `Status: Rejected`;
                    } else if (status === 'pending') {
                        dayDiv.title = `Status: Pending`;
                    } else if (status === 'approved') {
                        dayDiv.title = `Status: Approved`;
                    }
                }
                
                dayDiv.addEventListener('click', () => handleDateClick(dayDiv, formattedDate));
                daysContainer.appendChild(dayDiv);
            }
            highlightSelectedDates();
        }
        
        function handleDateClick(element, dateStr) {
            if (element.classList.contains('disabled-day')) {
                return; // Do nothing if it's a disabled day
            }
            
            const clickedDate = new Date(dateStr);

            const leaveForDay = leaveData.find(leave => {
                const leaveStart = new Date(leave.startDate);
                const leaveEnd = new Date(leave.endDate);
                return isDateInRange(clickedDate, leaveStart, leaveEnd);
            });
            
            if (leaveForDay && (leaveForDay.status === 'approved' || leaveForDay.status === 'rejected' || leaveForDay.status === 'pending' )) {
                // If the date is already an approved, rejected,  do not allow re-selection
                return;
            }

            if (!startSelection) {
                // First click sets the start date.
                startSelection = dateStr;
                endSelection = dateStr;
            } else if (new Date(dateStr) < new Date(startSelection)) {
                // Clicking a date before the start date resets the selection.
                startSelection = dateStr;
                endSelection = null;
            } else {
                // Any other click sets the end date.
                endSelection = dateStr;
            }

            // Ensure startSelection and endSelection are in the correct order.
            if (startSelection && endSelection && new Date(startSelection) > new Date(endSelection)) {
                [startSelection, endSelection] = [endSelection, startSelection];
            }

            // Check for weekends or existing leaves within the selected range.
            let tempDate = new Date(startSelection);
            let hasIssue = false;
            while (tempDate <= new Date(endSelection)) {
                if (tempDate.getDay() === 0 || tempDate.getDay() === 6) {
                    hasIssue = true;
                    displayError("Leave cannot be applied on weekends.");
                    break;
                }
                const isExistingLeave = leaveData.some(leave => {
                    const leaveStart = new Date(leave.startDate);
                    const leaveEnd = new Date(leave.endDate);
                    return isDateInRange(tempDate, leaveStart, leaveEnd);
                });
                if (isExistingLeave) {
                    hasIssue = true;
                    displayError("One or more of the selected dates is already a leave day.");
                    break;
                }
                tempDate.setDate(tempDate.getDate() + 1);
            }

            if (hasIssue) {
                startSelection = null;
                endSelection = null;
            } else {
                // If the selection is valid, update the hidden input fields.
                document.getElementById('startDate').value = startSelection;
                document.getElementById('endDate').value = endSelection || startSelection;
                // Clear any previous error messages.
                document.querySelector('.error').style.display = 'none';
            }

            highlightSelectedDates();
        }
        
        function highlightSelectedDates() {
            document.querySelectorAll('.calendar-day').forEach(dayDiv => {
                dayDiv.classList.remove('selection-highlight');
            });
            
            if (startSelection) {
                const start = new Date(startSelection);
                const end = endSelection ? new Date(endSelection) : new Date(startSelection);
                
                const tempDate = new Date(start);
                while (tempDate <= end) {
                    const formattedDate = tempDate.getFullYear() + '-' + (tempDate.getMonth() + 1).toString().padStart(2, '0') + '-' + tempDate.getDate().toString().padStart(2, '0');
                    const dayDiv = document.querySelector(`.calendar-day[data-date="${formattedDate}"]`);
                    if (dayDiv && !dayDiv.classList.contains('disabled-day')) {
                        dayDiv.classList.add('selection-highlight');
                    }
                    tempDate.setDate(tempDate.getDate() + 1);
                }
            }
        }
        
        function changeMonth(delta) {
            currentDate.setMonth(currentDate.getMonth() + delta);
            renderCalendar();
        }

        function isDateInRange(date, startDate, endDate) {
            date.setHours(0,0,0,0);
            startDate.setHours(0,0,0,0);
            endDate.setHours(0,0,0,0);
            return date >= startDate && date <= endDate;
        }

        function showPopup(status, reason) {
            document.getElementById('rejectionText').innerHTML = `<strong>Status:</strong> ${status}<br><strong>Reason:</strong> ${reason}`;
            document.getElementById('rejectionPopup').style.display = 'block';
            document.getElementById('overlay').style.display = 'block';
        }
        
        function hidePopup() {
            document.getElementById('rejectionPopup').style.display = 'none';
            document.getElementById('overlay').style.display = 'none';
        }

        startDateInput.addEventListener('change', () => {
            startSelection = startDateInput.value;
            endSelection = endDateInput.value;
            highlightSelectedDates();
        });
        endDateInput.addEventListener('change', () => {
            startSelection = startDateInput.value;
            endSelection = endDateInput.value;
            highlightSelectedDates();
        });

        document.addEventListener('DOMContentLoaded', () => {
            renderCalendar();
        });

        function toggleContent(element) {
            const content = element.nextElementSibling;
            if (content.style.display === "block") {
                content.style.display = "none";
            } else {
                content.style.display = "block";
            }
        }
        
        startDateInput.addEventListener('change', validateDates);
        endDateInput.addEventListener('change', validateDates);

        function validateDates() {
            // Clear previous errors
            let errorDiv = document.querySelector('.error');
            if (errorDiv) {
                errorDiv.style.display = 'none';
            }
            
            const startDateStr = startDateInput.value;
            const endDateStr = endDateInput.value;

            if (!startDateStr || !endDateStr) {
                return; // Don't validate until both dates are selected
            }

            const start = new Date(startDateStr);
            const end = new Date(endDateStr);

            // Check if start date is after end date
            if (start > end) {
                displayError("End Date must be after Start Date.");
                return;
            }

            // Calculate leave duration
            const diffInDays = Math.floor((end - start) / (1000 * 60 * 60 * 24)) + 1;

            // Check for yearly leave limit
            const totalYearlyLeave = <%= leaveBalance != null ? leaveBalance.getTotalYearlyLeave() : 24 %>;
// This should be fetched dynamically
            const yearlyLeaveTaken = <%=leaveBalance != null ? leaveBalance.getYearlyLeaveTaken() : 0%>;
            const pendingLeaves = leaveData.filter(l => l.status === 'pending').length; // Simplified count

            if ((yearlyLeaveTaken + diffInDays + pendingLeaves) > totalYearlyLeave) {
                displayError(`The applied leave duration exceeds the yearly limit of ${totalYearlyLeave} days. You have ` + (totalYearlyLeave - yearlyLeaveTaken - pendingLeaves) + ` days remaining this year.`);
                return;
            }

            // Check for weekends and existing leaves
            let tempDate = new Date(start);
            while (tempDate <= end) {
                if (tempDate.getDay() === 0 || tempDate.getDay() === 6) {
                    displayError("Leave cannot be applied on weekends.");
                    return;
                }

                const formattedDate = tempDate.getFullYear() + '-' + (tempDate.getMonth() + 1).toString().padStart(2, '0') + '-' + tempDate.getDate().toString().padStart(2, '0');
                const isExistingLeave = leaveData.some(leave => {
                    const leaveStart = new Date(leave.startDate);
                    const leaveEnd = new Date(leave.endDate);
                    return isDateInRange(tempDate, leaveStart, leaveEnd);
                });

                if (isExistingLeave) {
                    displayError("One or more of the selected dates is already a leave day.");
                    return;
                }

                tempDate.setDate(tempDate.getDate() + 1);
            }

            // If all checks pass, clear any errors
            if (errorDiv) {
                errorDiv.style.display = 'none';
            }
            
            // Update calendar highlight if validation passes
            highlightSelectedDates();
        }
        
        function displayError(message) {
            let errorDiv = document.querySelector('.error');
            if (!errorDiv) {
                errorDiv = document.createElement('div');
                errorDiv.classList.add('message', 'error');
                document.querySelector('form').prepend(errorDiv);
            }
            errorDiv.textContent = message;
            errorDiv.style.display = 'block';
        }
    </script>
	</script>
</body>
</html>
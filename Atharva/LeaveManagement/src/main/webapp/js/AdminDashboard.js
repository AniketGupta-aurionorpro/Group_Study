let indianHolidays = {};
let selectedDate = null;
let calendar; // keep reference to calendar

// Fetch holidays from servlet and render list + calendar
function fetchHolidays() {
	fetch("holidays")
		.then(res => res.json())
		.then(data => {
			// Convert list into map {date: reason}
			indianHolidays = {};
			data.forEach(h => {
				indianHolidays[h.date] = h.reason;
			});

			renderHolidayList();
			refreshCalendar(); // repaint cells with updated holidays
		});
}

function renderHolidayList() {
	const listEl = document.getElementById("holiday-list");
	listEl.innerHTML = "";

	Object.keys(indianHolidays).forEach(date => {
		const li = document.createElement("li");

		const text = document.createElement("span");
		text.textContent = `${date} - ${indianHolidays[date]}`;

		const cancelBtn = document.createElement("button");
		cancelBtn.textContent = "Cancel";
		cancelBtn.classList.add("cancel-btn");
		cancelBtn.onclick = function() {
			if (confirm(`Remove holiday on ${date}?`)) {
				deleteHoliday(date);
			}
		};

		li.appendChild(text);
		li.appendChild(cancelBtn);
		listEl.appendChild(li);
	});
}

// Calendar setup
function initCalendar() {
	const calendarEl = document.getElementById("calendar");
	calendar = new FullCalendar.Calendar(calendarEl, {
		initialView: 'dayGridMonth',
		height: 'auto',
		selectable: true,
		firstDay: 0, // Sunday
		headerToolbar: { left: 'prev,next', center: 'title', right: 'today' },

		selectAllow: function(info) {
			const dateStr = formatDateLocal(info.start);
			const day = info.start.getDay();
			if (day === 0 || day === 6)
				return false;
			if (indianHolidays[dateStr])
				return false;
			return true;
		},


		dateClick: function(info) {
			selectedDate = info.dateStr;

			const day = info.date.getDay();
			const actionDiv = document.getElementById("holiday-action");

			if (day !== 0 && day !== 6 && !indianHolidays[selectedDate]) {
				actionDiv.style.display = "block";
				loadLeavesByDate(selectedDate);
				document.getElementById("holidayReason").value = ""; // reset box
			} else {
				actionDiv.style.display = "none";
			}
		},

		dayCellDidMount: function(info) {
			paintDayCell(info);
		}
	});

	calendar.render();
}

// Paint weekends + holidays
function paintDayCell(info) {
	const day = info.date.getDay();
	const selectedDate = formatDateLocal(info.date);
	
	if (day === 0 || day === 6) {
		info.el.style.backgroundColor = "#f8d7da";
		info.el.title = "Weekend";
	}
	if (indianHolidays[selectedDate]) {
		info.el.style.backgroundColor = "#fff3cd";
		info.el.style.color = "#d9534f";
		info.el.title = indianHolidays[selectedDate];

		const reasonEl = document.createElement("div");
		reasonEl.style.fontSize = "0.7em";
		reasonEl.style.fontWeight = "bold";
		reasonEl.style.marginTop = "2px";
		reasonEl.innerText = indianHolidays[selectedDate];
		info.el.appendChild(reasonEl);
	}
}

// Refresh calendar cells (after add/delete)
function refreshCalendar() {
	calendar.destroy();
	initCalendar();
}

// Add holiday
document.addEventListener("DOMContentLoaded", function() {
	fetchHolidays();
	initCalendar();

	const markHolidayBtn = document.getElementById("markHolidayBtn");
	const reasonInput = document.getElementById("holidayReason");

	markHolidayBtn.addEventListener("click", function() {
		if (!selectedDate) return;
		const reason = reasonInput.value.trim();
		if (!reason) {
			alert("Please enter a reason.");
			return;
		}

		fetch("holidays", {
			method: "POST",
			headers: { "Content-Type": "application/x-www-form-urlencoded" },
			body: `date=${selectedDate}&reason=${encodeURIComponent(reason)}&action=add`
		})
			.then(res => res.json())
			.then(data => {
				if (data.success) {
					indianHolidays[selectedDate] = reason;
					renderHolidayList();
					reasonInput.value = "";
					document.getElementById("holiday-action").style.display = "none";
					refreshCalendar(); // repaint immediately
					snackbarFunction();
					console.log("update");
				} else {
					alert("Failed to add holiday.");
				}
			})
			.catch(err => {
				console.error("Error adding holiday:", err);
				alert("An error occurred while adding the holiday.");
			});
	});
});

// Delete holiday
function deleteHoliday(date) {
	fetch("holidays", {
		method: "POST",
		headers: { "Content-Type": "application/x-www-form-urlencoded" },
		body: `date=${date}&action=delete`
	})
		.then(res => res.json())
		.then(data => {
			if (data.success) {
				delete indianHolidays[date];
				renderHolidayList();
				refreshCalendar(); // repaint immediately
				snackbarFunction();
			} else {
				alert("Failed to remove holiday.");
			}
		})
		.catch(err => {
			console.error("Error deleting holiday:", err);
			alert("An error occurred while removing the holiday.");
		});
}

function loadLeavesByDate(date) {
	const leaveList = document.getElementById("leave-list");
	leaveList.innerHTML = `<p>Loading employees on leave for <b>${date}</b>...</p>`;

	fetch(`leavesByDate?date=${date}`)
		.then(res => res.json())
		.then(data => {
			if (data.length === 0) {
				leaveList.innerHTML = `<p>No employees on leave for <b>${date}</b>.</p>`;
				return;
			}

			let html = `<h3>Employees on leave for ${date}</h3><ul>`;
			data.forEach(l => {
				html += `<li><b>${l.user.name}</b> (${l.user.role}) - Reason: ${l.reason}</li>`;
			});
			html += "</ul>";

			leaveList.innerHTML = html;
		})
		.catch(err => {
			console.error("Error fetching leaves:", err);
			leaveList.innerHTML = `<p style="color:red">Failed to load leave data.</p>`;
		})
}

function formatDateLocal(date) {
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, "0");
    const d = String(date.getDate()).padStart(2, "0");
    return `${y}-${m}-${d}`;
}

var modal = document.getElementById("notifModal");
var btn = document.getElementById("notifBtn");
var span = document.getElementsByClassName("close")[0];
console.log("modal loaded");

// When the user clicks the button, open the modal 
btn.onclick = function() {
  modal.style.display = "block";
}

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
  modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
}

document.addEventListener("DOMContentLoaded", () => {
    // Mark all as read
    document.getElementById("markAllReadBtn").addEventListener("click", () => {
        fetch("notifications?action=markAllRead", { method: "POST" })
            .then(res => res.json())
            .then(data => { if (data.success) location.reload(); });
    });

    // Delete all read
    document.getElementById("deleteAllReadBtn").addEventListener("click", () => {
        fetch("notifications?action=deleteAllRead", { method: "POST" })
            .then(res => res.json())
            .then(data => { if (data.success) location.reload(); });
    });

    // Mark single notification as read
    document.querySelectorAll(".mark-read-btn").forEach(btn => {
        btn.addEventListener("click", function () {
            const notifId = this.closest(".notif-item").dataset.id;
            fetch("notifications?action=markRead&id=" + notifId, { method: "POST" })
                .then(res => res.json())
                .then(data => { if (data.success) location.reload(); });
        });
    });

    // Delete single notification
    document.querySelectorAll(".delete-btn").forEach(btn => {
        btn.addEventListener("click", function () {
            const notifId = this.closest(".notif-item").dataset.id;
            fetch("notifications?action=delete&id=" + notifId, { method: "POST" })
                .then(res => res.json())
                .then(data => { if (data.success) location.reload(); });
        });
    });
});

function snackbarFunction() {
  var x = document.getElementById("snackbar");
  x.className = "show";
  setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}
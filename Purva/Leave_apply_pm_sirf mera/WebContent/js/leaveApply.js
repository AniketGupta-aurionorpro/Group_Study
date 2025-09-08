let currentDate = new Date();
const daysContainer = document.getElementById('calendarGrid');
const monthYearDisplay = document.getElementById('currentMonthYear');
const startDateInput = document.getElementById('startDate');
const endDateInput = document.getElementById('endDate');
const WEEK_DAYS = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

// The leaveData array and other dynamic values are passed from the JSP file
// in a script tag before this file is loaded.

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
            if (status === 'rejected') {
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
        return;
    }
    
    const clickedDate = new Date(dateStr);

    const leaveForDay = leaveData.find(leave => {
        const leaveStart = new Date(leave.startDate);
        const leaveEnd = new Date(leave.endDate);
        return isDateInRange(clickedDate, leaveStart, leaveEnd);
    });
    
    if (leaveForDay && (leaveForDay.status === 'approved' || leaveForDay.status === 'rejected' || leaveForDay.status === 'pending' )) {
        return;
    }

    if (!startSelection) {
        startSelection = dateStr;
        endSelection = dateStr;
    } else if (new Date(dateStr) < new Date(startSelection)) {
        startSelection = dateStr;
        endSelection = null;
    } else {
        endSelection = dateStr;
    }

    if (startSelection && endSelection && new Date(startSelection) > new Date(endSelection)) {
        [startSelection, endSelection] = [endSelection, startSelection];
    }

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
        document.getElementById('startDate').value = startSelection;
        document.getElementById('endDate').value = endSelection || startSelection;
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
    let errorDiv = document.querySelector('.error');
    if (errorDiv) {
        errorDiv.style.display = 'none';
    }
    
    const startDateStr = startDateInput.value;
    const endDateStr = endDateInput.value;

    if (!startDateStr || !endDateStr) {
        return;
    }

    const start = new Date(startDateStr);
    const end = new Date(endDateStr);

    if (start > end) {
        displayError("End Date must be after Start Date.");
        return;
    }

    const diffInDays = Math.floor((end - start) / (1000 * 60 * 60 * 24)) + 1;
    const pendingLeaves = leaveData.filter(l => l.status === 'pending').length;

    if ((yearlyLeaveTaken + diffInDays + pendingLeaves) > totalYearlyLeave) {
        displayError(`The applied leave duration exceeds the yearly limit of ${totalYearlyLeave} days. You have ` + (totalYearlyLeave - yearlyLeaveTaken - pendingLeaves) + ` days remaining this year.`);
        return;
    }

    let tempDate = new Date(start);
    while (tempDate <= end) {
        if (tempDate.getDay() === 0 || tempDate.getDay() === 6) {
            displayError("Leave cannot be applied on weekends.");
            return;
        }

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

    if (errorDiv) {
        errorDiv.style.display = 'none';
    }
    
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
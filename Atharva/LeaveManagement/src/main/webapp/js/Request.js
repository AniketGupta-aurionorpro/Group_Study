function takeLeaveAction(leaveId, action, reason = "Leave Approved") {
    const params = new URLSearchParams();
    params.append("leaveId", leaveId);
    params.append("action", action);
    params.append("reason", reason);

    fetch("leaveAction", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: params.toString()
    })
    .then(res => res.json())
    .then(result => {
        if (result.success) {
            const card = document.querySelector(`.request-card input[value='${leaveId}']`)
                                .closest(".request-card");
            const form = card.querySelector("form");
            if (form) form.remove();

            const statusEl = document.createElement("p");
            statusEl.innerHTML = `Status: <b>${action}</b> ${reason ? " - " + reason : ""}`;
            card.appendChild(statusEl);

            card.classList.add("resolved");
        } else {
            alert(result.message);
        }
    })
    .catch(err => {
        console.error("Error updating leave:", err);
        alert("Something went wrong while updating leave.");
    });
}

function showRejectBox(leaveId) {
    // show rejection box
    const form = document.getElementById(`leaveForm-${leaveId}`);
    const rejectionDiv = form.querySelector(".rejection");
    rejectionDiv.style.display = "block";
}

function confirmReject(leaveId) {
    const reasonInput = document.getElementById(`rejectReason-${leaveId}`);
    const reason = reasonInput.value.trim();

    if (!reason) {
        alert("Please enter a rejection reason.");
        return;
    }

    takeLeaveAction(leaveId, 'REJECT', reason);
}

var coll = document.getElementsByClassName("collapsible");

for (let i = 0; i < coll.length; i++) {
  coll[i].addEventListener("click", function () {
	  console.log("clicked");
    this.classList.toggle("active");
    var content = this.nextElementSibling;

    if (content.style.maxHeight && content.style.maxHeight !== "0px") {
      content.style.maxHeight = null;
    } else {
      content.style.maxHeight = content.scrollHeight + "px";
    }
  });
}

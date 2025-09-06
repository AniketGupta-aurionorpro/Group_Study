function takeLeaveAction(leaveId, action) {
    fetch("leaveAction", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: `leaveId=${leaveId}&action=${action}`
    })
    .then(res => res.json())
    .then(result => {
        if (result.success) {
            // find the card for this request
            const card = document.querySelector(`.request-card input[value='${leaveId}']`)
                                .closest(".request-card");

            // remove the form with buttons
            const form = card.querySelector("form");
            if (form) form.remove();

            // add status text
            const statusEl = document.createElement("p");
            statusEl.innerHTML = `Status: <b>${action}</b>`;
            card.appendChild(statusEl);

            // change background to grey
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

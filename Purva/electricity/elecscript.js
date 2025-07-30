let entries = [];

function generateBill() {
  const firstName = document.getElementById("firstname").value.trim();
  const lastName = document.getElementById("lastname").value.trim();
  const email = document.getElementById("email").value.trim();
  const units = parseFloat(document.getElementById("units").value);

  if (!firstName || !lastName || !email || isNaN(units)) {
    alert("Please fill in all fields correctly.");
    return;
  }

  const total = calculateBill(units);

  const entry = {
    firstName,
    lastName,
    email,
    units,
    total
  };

  entries.push(entry);
  renderEntries();
}

function calculateBill(units) {
  if (units <= 50) {
    return units * 3;
  } else if (units <= 150) {
    return 50 * 3 + (units - 50) * 5;
  } else if (units <= 250) {
    return 50 * 3 + 100 * 5 + (units - 150) * 7;
  } else {
    return 50 * 3 + 100 * 5 + 100 * 7 + (units - 250) * 10;
  }
}

function renderEntries() {
  const result = document.getElementById("result");
  result.innerHTML = ""; // Clear old

  entries.forEach((entry, index) => {
    const line = document.createElement("div");
    line.textContent = `Name: ${entry.firstName} ${entry.lastName}, Email: ${entry.email}, Units: ${entry.units}, Total Bill: ₹${entry.total.toFixed(2)}`;
    result.appendChild(line);
  });

  result.style.display = "block";
}

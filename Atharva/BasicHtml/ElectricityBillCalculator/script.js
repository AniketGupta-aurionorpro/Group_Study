function calculateBill() {
  const unitsInput = document.getElementById("units");
  let units = parseInt(unitsInput.value, 10);

  if (isNaN(units) || units < 0) {
    alert("Please enter a valid number of units.");
    return;
  }

 let baseBill = 0;
  const price1 = 1.20;
  const price2 = 1.50;
  const price3 = 1.80;
  const price4 = 2.00;

  const limit1 = 199;
  const limit2 = 399;
  const limit3 = 599;

  if (units <= limit1) {
    baseBill = units * price1;
  } else if (units <= limit2) {
    baseBill = (limit1 * price1) + (units - limit1) * price2;
  } else if (units <= limit3) {
    baseBill = (limit1 * price1) + (200 * price2) + (units - limit2) * price3;
  } else {
    baseBill = (limit1 * price1) + (200 * price2) + (200 * price3) + (units - limit3) * price4;
  }

  let surcharge = 0;
  const surchargePercent = 0.15;
  let totalBill = baseBill;
  const minAmount = 100;
  let messages = [];

  // Apply surcharge if applicable
  if (baseBill > 500) {
    surcharge = baseBill * surchargePercent;
    totalBill += surcharge;
  }

  // Apply minimum bill rule
  if (totalBill < minAmount) {
    totalBill = minAmount;
    messages.push(`Minimum bill of ₹${minAmount} applied.`);
  }

  // Display result
  result.innerHTML = `
    <p><strong>Units Consumed:</strong> ${units}</p>
    <p><strong>Base Price:</strong> ₹${baseBill.toFixed(2)}</p>
    ${surcharge > 0 ? `<p><strong>Surcharge (15%):</strong> ₹${surcharge.toFixed(2)}</p>` : ""}
    ${messages.length ? `<p>${messages.join("<br>")}</p>` : ""}
    <p><strong>Total Bill:</strong> ₹${totalBill.toFixed(2)}</p>
  `;
}
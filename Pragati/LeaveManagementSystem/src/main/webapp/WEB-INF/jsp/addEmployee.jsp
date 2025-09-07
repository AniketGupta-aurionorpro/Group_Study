<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Employee / Manager</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/admin.css">
    <script>
        function toggleManagerField() {
            const roleSelect = document.getElementById("role");
            const managerField = document.getElementById("managerField");
            const managerInput = document.getElementById("managerId");

            if (roleSelect.value === "EMPLOYEE") {
                managerField.style.display = "block";
                managerInput.value = "";
                managerInput.readOnly = false;
            } else if (roleSelect.value === "MANAGER") {
                managerField.style.display = "block";
                managerInput.value = "1"; // admin id
                managerInput.readOnly = true;
            } else {
                managerField.style.display = "none";
                managerInput.value = "";
            }
        }
    </script>
</head>
<body onload="toggleManagerField()">

<div class="main">
    <h2>Add Employee / Manager</h2>

    <p class="success">
        <%= request.getParameter("success") != null ? "User added successfully!" : "" %>
    </p>
    <p class="error">
        <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
    </p>

    <form method="post" action="<%= request.getContextPath() %>/admin/addUser">
        <label>Name:</label>
        <input type="text" name="name" required><br><br>

        <label>Email:</label>
        <input type="email" name="email" required><br><br>

        <label>Password:</label>
        <input type="password" name="password" required><br><br>

        <label>Role:</label>
        <select name="role" id="role" required onchange="toggleManagerField()">
            <option value="EMPLOYEE">Employee</option>
            <option value="MANAGER">Manager</option>
        </select><br><br>

        <div id="managerField" style="display:none;">
            <label>Manager ID:</label>
            <input type="number" name="manager_id" id="managerId">
            <br><br>
        </div>

        <label>Date of Joining:</label>
        <input type="date" name="joinedDate" required><br><br>

        <button type="submit">Add</button>
    </form>
</div>

</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.leave.system.model.User" %>
<%
    String userName = (String) session.getAttribute("userName");
    String role = (String) session.getAttribute("role");

    if (userName == null || !"ADMIN".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    List<User> users = (List<User>) request.getAttribute("users");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Users</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/admin.css">
    <script>
        function toggleEditForm(id) {
            const formDiv = document.getElementById('editForm-' + id);
            formDiv.style.display = (formDiv.style.display === 'block') ? 'none' : 'block';
        }

        function filterUsers() {
            const filter = document.getElementById('searchInput').value.toLowerCase();
            const table = document.getElementById('usersTable');
            const tr = table.getElementsByTagName('tr');

            for (let i = 1; i < tr.length; i += 2) { // skip edit form rows
                const tdName = tr[i].getElementsByTagName('td')[1];
                const tdEmail = tr[i].getElementsByTagName('td')[2];
                if (tdName && tdEmail) {
                    const name = tdName.textContent.toLowerCase();
                    const email = tdEmail.textContent.toLowerCase();
                    if (name.includes(filter) || email.includes(filter)) {
                        tr[i].style.display = '';
                        tr[i + 1].style.display = 'none'; // hide edit form when filtered
                    } else {
                        tr[i].style.display = 'none';
                        tr[i + 1].style.display = 'none';
                    }
                }
            }
        }
    </script>
</head>
<body>
<div class="main">
    <h2>Edit Users</h2>

    <% String errorMsg = (String) session.getAttribute("errorMessage");
       String successMsg = (String) session.getAttribute("successMessage");
       if(errorMsg != null) { %>
           <p class="error"><%= errorMsg %></p>
           <% session.removeAttribute("errorMessage"); %>
    <% } %>
    <% if(successMsg != null) { %>
           <p class="success"><%= successMsg %></p>
           <% session.removeAttribute("successMessage"); %>
    <% } %>

    <input type="text" id="searchInput" class="searchBox" placeholder="Search by name or email..." onkeyup="filterUsers()">

    <% if (users != null && !users.isEmpty()) { %>
        <table id="usersTable">
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Manager ID</th>
                <th>Action</th>
            </tr>
            <% for (User u : users) {
                   if ("ADMIN".equals(u.getRole())) continue; // skip admin
            %>
            <tr>
                <td><%= u.getId() %></td>
                <td><%= u.getName() %></td>
                <td><%= u.getEmail() %></td>
                <td><%= u.getRole() %></td>
                <td><%= u.getManagerId() != null ? u.getManagerId() : "-" %></td>
                <td>
                    <button type="button" class="editBtn" onclick="toggleEditForm(<%= u.getId() %>)">Edit</button>
                    <form method="post" action="<%= request.getContextPath() %>/admin/deleteUser" style="display:inline;" 
                          onsubmit="return confirm('Are you sure you want to delete <%= u.getName() %>?');">
                        <input type="hidden" name="id" value="<%= u.getId() %>">
                        <input type="hidden" name="role" value="<%= u.getRole() %>">
                        <button type="submit" class="deleteBtn">Delete</button>
                    </form>
                </td>
            </tr>
            <tr id="editForm-<%= u.getId() %>" class="editForm">
                <td colspan="6">
                    <form method="post" action="<%= request.getContextPath() %>/admin/editUser">
                        <input type="hidden" name="id" value="<%= u.getId() %>">
                        Name: <input type="text" name="name" value="<%= u.getName() %>" required>
                        Email: <input type="email" name="email" value="<%= u.getEmail() %>" required>
                        Password: <input type="password" name="password" value="<%= u.getPassword() %>">
                        Role: 
                        <select name="role">
                            <option value="EMPLOYEE" <%= "EMPLOYEE".equals(u.getRole()) ? "selected" : "" %>>Employee</option>
                            <option value="MANAGER" <%= "MANAGER".equals(u.getRole()) ? "selected" : "" %>>Manager</option>
                        </select>
                        Manager ID: 
                        <% if ("MANAGER".equals(u.getRole())) { %>
                            <!-- For MANAGER, lock the field -->
                            <input type="number" name="managerId" value="<%= u.getManagerId() != null ? u.getManagerId() : "1" %>" readonly>
                        <% } else { %>
                            <!-- For EMPLOYEE, allow editing -->
                            <input type="number" name="managerId" value="<%= u.getManagerId() != null ? u.getManagerId() : "1" %>">
                        <% } %>
                        <button type="submit">Update</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </table>
    <% } else { %>
        <p>No users found.</p>
    <% } %>
</div>
</body>
</html>

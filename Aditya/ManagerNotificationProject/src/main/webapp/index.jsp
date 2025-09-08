<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Manager Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow-sm p-4 mx-auto" style="max-width: 400px;">
        <h2 class="text-primary mb-4 text-center">Manager Login</h2>
        <form method="post" action="LoginServlet">
            <div class="mb-3">
                <input type="email" name="email" class="form-control" placeholder="Email" required/>
            </div>
            <div class="mb-3">
                <input type="password" name="password" class="form-control" placeholder="Password" required/>
            </div>
            <button type="submit" class="btn btn-primary w-100">Login</button>
        </form>
        <%
            String error = request.getParameter("error");
            if(error != null){
        %>
            <div class="alert alert-danger mt-3 text-center"><%=error%></div>
        <% } %>
    </div>
</div>
</body>
</html>

<%@ page language="java"%>
<%
    String userName = (String) session.getAttribute("userName");
    String role = (String) session.getAttribute("role");
    if (userName == null || !"EMPLOYEE".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<h2>Welcome Employee, <%= userName %>!</h2>
<p>This is the employee dashboard.</p>
<a href="<%= request.getContextPath() %>/login">Logout</a>

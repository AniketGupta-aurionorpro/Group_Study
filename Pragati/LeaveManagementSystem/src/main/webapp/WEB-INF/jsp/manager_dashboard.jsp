<%@ page language="java"%>
<%
    String userName = (String) session.getAttribute("userName");
    String role = (String) session.getAttribute("role");
    if (userName == null || !"MANAGER".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
<h2>Welcome Manager, <%= userName %>!</h2>
<p>This is the manager dashboard.</p>
<a href="<%= request.getContextPath() %>/login">Logout</a>

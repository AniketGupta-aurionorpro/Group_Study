<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<% 
    HttpSession currentSession = request.getSession(false);
	
	String name;
    if (currentSession != null) {
        name = (String) currentSession.getAttribute("studentName");
    } else {
        out.println("Session was invalidated. No attributes available.");
        name = null;
    }
%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h2>Dashboard for <%=name%></h2>
</body>
</html>
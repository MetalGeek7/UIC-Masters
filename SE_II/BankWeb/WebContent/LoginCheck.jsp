<%@page import="cs442.group2.BankingApplication.Customer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
</head>
<body>
	<%@ page import="java.util.Arrays"%>
	<%@ page import="java.util.List"%>
	<%@ page import="cs442.group2.BankingApplication.Account"%>
	<%@ page import="cs442.group2.BankingApplication.Customer"%>
	<%@ page import="cs442.group2.BankingApplication.BankConnect"%>

	<%
	
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		Customer cs = Customer.authenticate(userName, password);

		
		if (cs != null) {
			
			session.setAttribute("userName", userName);
			session.setAttribute("password", password);
			response.sendRedirect("Home.jsp");
		} else {
			
			response.sendRedirect("Error.jsp");
		}
	%>
</body>
</html>



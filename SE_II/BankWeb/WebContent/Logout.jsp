<%
	session.invalidate();
	response.sendRedirect(response.encodeRedirectURL("Home.jsp"));
%>
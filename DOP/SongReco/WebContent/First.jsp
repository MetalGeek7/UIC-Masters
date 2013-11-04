<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import ="Beans.*" %>
    <%@page import ="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<FORM NAME="testform" method="post" action="createUserServlet">

<table id="myTable" border="0" width="400" cellspacing="0" 
              cellpadding="0">

<tr id="row_1"  >
<td height="25" width="150" > LoginName </td>
<td><input type=text id="user_name" name="user_name" ></td>
</tr>

<tr>
<td><input type="submit" name="submit" value="Submit"></td>
<td><a href="NewUser.jsp">New User?</a></td>
</tr>

</table>
</FORM>
</body>
</html>

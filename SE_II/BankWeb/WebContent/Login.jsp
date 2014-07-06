<%@page import="cs442.group2.BankingApplication.Account"%>
<%@page import="cs442.group2.BankingApplication.Cart"%>
<%@page import="cs442.group2.BankingApplication.Credit"%>
<%@page import="cs442.group2.BankingApplication.Customer"%>
<%@page import="cs442.group2.BankingApplication.Item"%>
<%@page import="cs442.group2.BankingApplication.Order"%>
<%@page import="cs442.group2.BankingApplication.OrderDetail"%>
<%@page import="cs442.group2.BankingApplication.OrderPayment"%>
<%@page import="cs442.group2.BankingApplication.Transaction"%>
<%@page
	import="cs442.group2.BankingApplication.exceptions.InsufficientBalanceException"%>
<%@page
	import="cs442.group2.BankingApplication.exceptions.InvalidCustomerException"%>
<%@page
	import="cs442.group2.BankingApplication.exceptions.ItemNotAvailableException"%>
<%@page
	import="cs442.group2.BankingApplication.exceptions.RewardsCustomerException"%>
<%@page import="cs442.group2.BankingApplication.helpers.AccountChoice"%>
<%@page import="java.util.*"%>

<%
	String errorLogin = "OK";

	String submitValue = (String) request.getParameter("submit");

	if (submitValue != null && submitValue.equalsIgnoreCase("login")) {
		String userName = (String) request.getParameter("userName");
		String password = (String) request.getParameter("password");
		
		//System.out.println("Inside form processing");
		
		Customer check = Customer.authenticate(userName, password);
		if(check != null){
			//System.out.println(check);
			session.setAttribute("userName", userName);
			session.setAttribute("password", password);
			response.sendRedirect(response.encodeRedirectURL("Home.jsp"));
		} else {
			errorLogin = "Incorrect Username / Password";
		}
		
		
	} else {
		errorLogin = "";
	}
	
	if(!errorLogin.equals("OK")){
%>

<%@include file="templates/header.jsp"%>
<center>
	<%
		if (!errorLogin.equals("OK")) {
			out.println(errorLogin);
		}
	%>

	<h2>Enter Login details</h2>
	<br />
	<form action="Login.jsp" method="post">

		<table>
			<tr>
				<td>User Name</td>
				<td><input type="text" name="userName">
				</td>
			</tr>
			<tr>
				<td>Password</td>
				<td><input type="password" name="password">
				</td>
			</tr>
			<tr>

				<td colspan="2" style="text-align: right"><input type="submit"
					value="Login" name="submit">
				</td>
			</tr>
		</table>
	</form>
</center>
<%@include file="templates/footer.jsp"%>
<% } %>
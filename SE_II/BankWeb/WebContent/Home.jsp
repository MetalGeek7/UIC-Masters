<%@page import="cs442.group2.BankingApplication.Account"%>
<%@page import="java.util.List"%>
<%@page import="cs442.group2.BankingApplication.Customer"%>
<%
	String userName = (String)session.getAttribute("userName");
	String password = (String)session.getAttribute("password");
	if(userName == null || password == null){
		response.sendRedirect(response.encodeRedirectURL("Login.jsp"));
	}
	else{	
		
	Customer customer = Customer.authenticate(userName, password);
	
	// Handle the logic here such that if not logged in then go to Login.jsp, also if by some chance customer is null go to Login.jsp
	
	
%>
<%@include file="templates/header.jsp"%>
<table width="100%">
	<tr>
		<td><a href="Home.jsp"> Home</a> | <a href="Transfer.jsp">Transfer
				Money</a> | <a href="ShoppingHome.jsp">Shopping Portal</a> | <a
			href="Logout.jsp">Logout</a></td>
		<td align="right"><a href="ViewCart.jsp">Cart</a> : <%=customer.getCart().getAllItems().size() %></td>
	</tr>

</table>
<br>
<center>
	<table width="70%" border="0" align="center" cellpadding="2"
		cellspacing="2">

		<tr>
			<td align="right" style="width: 20%">User Name :</td>
			<td style="width: 80%"><%=customer.getUserName()%>
				<form action="OrderHistory.jsp" method="post"
					style="display: inline">
					<input type="submit" value="Show Order History">
				</form></td>
		</tr>

		<tr>
			<td style="vertical-align: top; text-align: right;">Account
				Details :</td>
			<td>
				<table border="1" style="width: 100%" cellpadding="5">
					<tr>
						<th>Account Number</th>
						<th>Account Type</th>
						<th>Balance</th>
						<th>Action</th>
					</tr>
					<%
						List<Account> accounts = customer.getAccounts();
						for(Account account : accounts){
					%>
					<tr>
						<td><%=account.getAccountID() %></td>
						<td><%=account.getAccountType() %></td>
						<td><%=account.getBalance() %></td>
						<td>
							<form action="AccountHistory.jsp" method="get">
								<input type="hidden" name="accountID"
									value="<%=account.getAccountID() %>"> <input
									type="submit" value="Show Account History">
							</form></td>
					</tr>
					<%
						}
					%>
				</table></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>

		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
	</table>
</center>
<%@include file="templates/footer.jsp"%>
<% } %>
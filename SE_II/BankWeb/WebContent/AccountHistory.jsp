<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cs442.group2.BankingApplication.Transaction"%>
<%@page import="cs442.group2.BankingApplication.Account"%>
<%@page import="java.util.List"%>
<%@page import="cs442.group2.BankingApplication.Customer"%>
<%
	int accountID = -1; // Comment this line 
	if (request.getParameter("accountID") != null) {
		try {
			accountID = Integer.parseInt((String) request
					.getParameter("accountID"));
	
		} catch (Exception e) {
			accountID = -1;
		}
	}


	String userName = (String) session.getAttribute("userName"); // Get from session
	String password = (String) session.getAttribute("password"); // Get from session

	Customer customer = Customer.authenticate(userName, password);
	if (customer == null || accountID <= 1) {
		response.sendRedirect(response.encodeRedirectURL("Login.jsp"));
	} else {
		List<Account> accounts = customer.getAccounts();
		Account account = null;

		
		for (int i = 0; i < accounts.size(); i++) {
			if (accountID == accounts.get(i).getAccountID()) {
				account = accounts.get(i);
				break;
			}
		}
	
%>
<%@include file="templates/header.jsp"%>
<%
	if (account == null) {
%>
Account not present
<%
	} else {
			List<Transaction> transfers = account.accountHistory();
%>
<table width="100%">
	<tr>
		<td><a href="Home.jsp"> Home</a> | <a href="Transfer.jsp">Transfer
				Money</a> | <a href="ShoppingHome.jsp">Shopping Portal</a> | <a
			href="Logout.jsp">Logout</a>
		</td>
		<td align="right"><a href="ViewCart.jsp">Cart</a> : <%=customer.getCart().getAllItems().size()%></td>
	</tr>

</table>
<br>
<center>
	<table>
		<tr>
			<td align="right">Account User Name:</td>
			<td><%=customer.getUserName()%></td>
		</tr>
		<tr>
			<td align="right">Account Number:</td>
			<td>
				<%
					out.print(account.getAccountID() + " ("
									+ account.getAccountType() + ")");
				%>
			</td>
		</tr>
		<tr>
			<td align="right">Current Balance:</td>
			<td><%=account.getBalance()%></td>
		</tr>
	</table>

	<%
		if (transfers.size() > 0) {
	%>
	<br />
	<table border="1" cellpadding="5">
		<tr>
			<th>DATE</th>
			<th>Actions</th>
			<th>Amount</th>
			<th>Updated Balance</th>
		</tr>
		<%
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			double tempamount = account.getBalance();
						String action = "";
						for (Transaction transfer : transfers) {
							if (transfer.getToAccountID() == 1) {
								action = "Paid for order";
							} else {
								if (transfer.getFromAccountID() == accountID) {
									action = "Transfer to #"
											+ transfer.getToAccountID();
								} else
									action = "Transfer from #"
											+ transfer.getFromAccountID();
							}
		%>
		<tr>
			<td><%=format.format(transfer.getTimestamp())%></td>
			<td><%=action%></td>
			<td><%=transfer.getAmount()%></td>
			<td><%=tempamount%></td>
		</tr>
		<%
			if (transfer.getFromAccountID() == accountID) {
								tempamount += transfer.getAmount();
							} else
								tempamount -= transfer.getAmount();
						}
		%>
		<tr>
			<td></td>
			<td>Initial Deposit</td>
			<td><%=tempamount%></td>
			<td><%=tempamount%></td>
		</tr>
	</table>
	<%
		}
	%>
</center>
<%
	}
%>
<%@include file="templates/footer.jsp"%>
<% } %>
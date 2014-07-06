<%@page contentType="text/html" pageEncoding="UTF-8"
	errorPage="Error.jsp"%>
<%@page import="java.util.List"%>
<%@ page import="cs442.group2.BankingApplication.Account"%>
<%@ page import="cs442.group2.BankingApplication.Customer"%>
<%
	String userName = (String) session.getAttribute("userName");
	String password = (String) session.getAttribute("password");
	if (userName == null || password == null) {
		response.sendRedirect(response.encodeRedirectURL("Login.jsp"));
	} else {

		Customer customer = Customer.authenticate(userName, password);

		// Handle the logic here such that if not logged in then go to Login.jsp, also if by some chance customer is null go to Login.jsp
%>
<%@include file="templates/header.jsp"%>
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
<form action="TransactionResult.jsp" method="post">

	<table width="80%" border="0" align="center" cellpadding="2"
		cellspacing="2">
		<tr>
			<th colspan="3" bgcolor="#333333" scope="col"><font
				color="#FFFFFF">Please Select the Accounts for Transfer </font>
			</th>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><div align="right">Account User Name</div></td>


			<td>:</td>
			<td><%=customer.getUserName()%></td>
		</tr>


		<tr>
			<td width="43%"><div align="right">
					<strong>Select Source Account No.</strong>
				</div>
			</td>
			<td width="2%">:</td>
			<td width="55%"><input type="number" name="fromaccount">

			</td>
		</tr>

		<tr>
			<td><div align="right">
					<strong>Select Destination Account No. </strong>
				</div>
			</td>
			<td width="2%">:</td>
			<td width="55%"><input type="number" name="toaccount"></td>
		</tr>

		<tr>
			<td><div align="right">
					<strong>PIN. </strong>
				</div>
			</td>
			<td width="2%">:</td>
			<td width="55%"><input type="password" name="PIN"></td>
		</tr>

		<tr>
			<td><div align="right">
					<strong>Amount</strong>
				</div>
			</td>
			<td>:</td>
			<td><input name="Amount" type="text" id="Amount" size="10" /> $</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td><input type="submit" value="Transfer Money"></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
	</table>

</form>



<%@include file="templates/footer.jsp"%>
<%
	}
%>
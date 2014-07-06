<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="cs442.group2.BankingApplication.Customer"%>
<%@ page import="cs442.group2.BankingApplication.Account"%>
<%@ page import="cs442.group2.BankingApplication.Transaction"%>
<%@ page import="java.util.List"%>
<%
	String userName = (String) session.getAttribute("userName");
	String password = (String) session.getAttribute("password");
	double amt=0;
	Customer customer = Customer.authenticate(userName, password);
	if (customer == null) {
		response.sendRedirect(response.encodeRedirectURL("Login.jsp"));
	} else {
		String resultString = null;
		try{
		int fromacc = Integer.parseInt(request.getParameter("fromaccount"));
		int toacc = Integer.parseInt(request.getParameter("toaccount"));
		amt = Double.parseDouble(request.getParameter("Amount"));

		String PIN = request.getParameter("PIN");

		Account fromaccount = Account.getAccount(fromacc);
		Account toaccount = Account.getAccount(toacc);

		
		
		resultString = "" + fromaccount.toString() + "\n" + toaccount.toString()  + "\n" + amt;

		
		if (PIN != null && PIN.equals(fromaccount.getPin()) || true) {
			resultString = customer.transfer(fromaccount, toaccount,amt);
		}
		} catch(Exception e){
			// Formatting exception can take place.
			resultString = null;
		}

		if (resultString == null) {
			response.sendRedirect(response
					.encodeRedirectURL("Home.jsp"));
		} else {
%>
<%@include file="templates/header.jsp"%>
<table width="100%">
	<tr>
		<td><a href="Home.jsp"> Home</a> | <a href="Transfer.jsp">Transfer
				Money</a> | <a href="ShoppingHome.jsp">Shopping Portal</a> | <a
			href="Logout.jsp">Logout</a></td>
		<td align="right"><a href="ViewCart.jsp">Cart</a> : <%=customer.getCart().getAllItems().size()%></td>
	</tr>

</table>
<br>
<%
	StringBuilder result = new StringBuilder();
	String status[] = resultString.split("\n");
	if(status[0].equals("Success")){
		result.append(String.format("Your transfer of amount $%s was successfull.<br>", amt));
		result.append(String.format("Your reference number is: #%s", status[1]));
		//Your transfer of amount $ was successfull
		//Your reference number is: # 
	} else {
		result.append(String.format("Your transfer of amount $%s, failed.<br>", amt));
		result.append(String.format("Error:<br>%s", status[2]));
		
	}
	
%>
	<%=result.toString() %>
	<a href="Home.jsp"> Return to Home</a>
<%@include file="templates/footer.jsp"%>
<%
		}
	}
%>

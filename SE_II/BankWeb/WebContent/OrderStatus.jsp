<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="cs442.group2.BankingApplication.Item"%>
<%@page import="java.util.List"%>
<%@page import="cs442.group2.BankingApplication.Cart"%>
<%@page import="cs442.group2.BankingApplication.Customer"%>
<%@page import="cs442.group2.BankingApplication.Account"%>
<%@page import="cs442.group2.BankingApplication.Order"%>
<%@page import="cs442.group2.BankingApplication.helpers.AccountChoice"%>
<%

String userName = (String)session.getAttribute("userName"); // Get from session
String password = (String)session.getAttribute("password"); // Get from session

// Use session logic above and below
Customer customer = Customer.authenticate(userName, password);
Cart cart = customer.getCart();

String submitValue = request.getParameter("Place Order");
List<Account> accounts = customer.getAccounts();

ArrayList<AccountChoice> accChoices = new ArrayList<AccountChoice>();

String s="";
int x=0;
double checkingamt=0,savingamt=0,rewardamt=0,creditamt=0;

		if(request.getParameter("checkingsAmount")!=""&& request.getParameter("checkingsOption")!=null){
			 checkingamt = Double.parseDouble(request.getParameter("checkingsAmount"));
			//System.out.println(checkingamt);
			for (Account account : accounts) {
				if (account.getAccountType().equals("Checkings"))
				{
					x=account.getAccountID();
				}
			}
			Account acc1 = Account.getAccount(x);
			AccountChoice choice1 = new AccountChoice(acc1, checkingamt);
			accChoices.add(choice1);
		}
		
		if( request.getParameter("savingsAmount")!=""&& request.getParameter("savingsOption")!=null){
			 savingamt = Double.parseDouble(request.getParameter("savingsAmount"));
			//System.out.println(savingamt);
			for (Account account : accounts) {
				if (account.getAccountType().equals("Savings"))
				{
					x=account.getAccountID();
				}
			}
			Account acc1 = Account.getAccount(x);
			AccountChoice choice2 = new AccountChoice(acc1, savingamt);
			accChoices.add(choice2);
		}
		
		if( request.getParameter("rewardsAmount")!=""&& request.getParameter("rewardsOption")!=null){
			rewardamt = Double.parseDouble(request.getParameter("rewardsAmount"));
			//System.out.println(rewardamt);
			for (Account account : accounts) {
				if (account.getAccountType().equals("Rewards"))
				{
					x=account.getAccountID();
				}
			}
			Account acc1 = Account.getAccount(x);
			AccountChoice choice3 = new AccountChoice(acc1, rewardamt);
			accChoices.add(choice3);
		}
	
		if( request.getParameter("creditAmount")!=""&& request.getParameter("creditOption")!=null){
			creditamt = Double.parseDouble(request.getParameter("creditAmount"));
			//System.out.println(creditamt);
			Account account = Account.getAccountByCreditCardNumber(
					request.getParameter("cardNo"), request.getParameter("cvv2"));
			
			AccountChoice choice4 = new AccountChoice(account, creditamt);
			accChoices.add(choice4);
		}
		
		System.out.println(Arrays.toString(accChoices.toArray(new AccountChoice[0])));

		double amttotal= checkingamt+savingamt+rewardamt+creditamt;
		if (amttotal!=(double)cart.getItemTotalCost() || cart.getAllItems().size() == 0){
			response.sendRedirect(response.encodeRedirectURL("Checkout.jsp"));	
			
		}
		
		
		else {
			String orderstatus = customer.order(accChoices);
			//System.out.println("Order Status is : " + orderstatus);
			String status[] = orderstatus.split("\n");
			
			
			HashMap<String, String> errorMap = new HashMap<String, String>();
			
			errorMap.put("InsufficientBalanceException", "Insufficient Funds");
			errorMap.put("RewardsCustomerException",     "Wrong Rewards Account");
			errorMap.put("InvalidCustomerException",     "Portal User Cannot Make an Order");
			errorMap.put("ItemNotAvailableException",    "Item is currently unavailable");
			if(status.length == 3){
				errorMap.put("SQLException",                 status[2]);
				errorMap.put("Exception",                    status[2]);
			}


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
<table width="100%">
	
	<tr>
		<td align="left" style="width: 20%">User Name :<%=customer.getUserName()%></td>
		<td style="width: 80%">
			<!-- Order Status: <%=orderstatus%> -->
			<% if(status[0].equals("Success")) { %>
			<p style='color: green'>
				Your order was successfull <br>
				Your order number is # <%=status[1] %> 
			</p>
			<% }else{ %>
			<p style='color:red'>
				Your order failed <br>
				ERROR: <br><%=errorMap.get(status[1]) %>
			</p> 
			<% } %>
		</td>
	</tr>

</table>
<%@include file="templates/footer.jsp"%>
<%
	}
%>

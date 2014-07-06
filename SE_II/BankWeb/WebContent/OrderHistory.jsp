<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cs442.group2.BankingApplication.OrderDetail"%>
<%@page import="cs442.group2.BankingApplication.Order"%>
<%@page import="java.util.List"%>
<%@page import="cs442.group2.BankingApplication.Customer"%>
<%
	String userName = (String)session.getAttribute("userName"); // Get from session
	String password = (String)session.getAttribute("password"); // Get from session

	Customer customer = Customer.authenticate(userName, password);
	if(customer == null){
		response.sendRedirect(response.encodeRedirectURL("Login.jsp"));		
	} else {

	List<Order> orders = customer.orderHistory();
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
<table class="borderme" style="width: 100%;">

	<tr>
		<td style="width: 30%;">Username : <%
			out.println(customer.getUserName());
		%> <br> <br>Your Order History : <br>
		</td>

		<td style="width: 70%;"></td>
	</tr>

	<%
		if (orders.size() > 0) {
	%>



	<%
	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
		for (Order order : orders) {
				List<OrderDetail> orderDetails = order.getOrderDetails();
	%>

	<tr style="border-bottom: 1px solid black">
		<td style="width: 30%;">
			<%
				out.print(format.format(order.getDateTime()));
			%> <br>Order Number : # <%
 	out.print(order.getOrderID());
 %> <br> Total : $<%
 	out.print(order.getTotalAmount());
 %>
		</td>
		<td style="width: 70%;">
			<table style="width: 100%; border: 1px solid black;">
				<tr>
					<th>Item Name</th>
					<th>Item Cost</th>
					<th>Quantity Ordered</th>
				</tr>
				<%
					for (OrderDetail detail : orderDetails) {
				%>
				<tr style="text-align: center">
					<td><%=detail.getItem().getItemName()%></td>
					<td><%=detail.getItem().getItemCost()%></td>
					<td><%=detail.getQuantityPurchased()%></td>
				</tr>

				<%
					}
				%>

			</table>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<hr>
		</td>
	</tr>

	<%
		}
	%>


	<%
		}
	%>

</table>
<%@include file="templates/footer.jsp"%>
<% } %>
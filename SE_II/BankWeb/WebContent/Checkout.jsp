<%@page import="cs442.group2.BankingApplication.Cart"%>
<%@page import="cs442.group2.BankingApplication.Customer"%>
<%
	String userName = (String) session.getAttribute("userName");
	String password = (String) session.getAttribute("password");

	// Use session logic above and below
	Customer customer = Customer.authenticate(userName, password);
	Cart cart = customer.getCart();

	//List<Item> items = cart.getAllItems();
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
<form action="OrderStatus.jsp" method="post">


	<table style="width: 100%">
		<tr>

			<td style="width: 100px;">Option 1<input type="checkbox"
				name="checkingsOption"></td>
			<td><label for="Checkings"> Checkings </label></td>

			<td>Amount <input type="text" name="checkingsAmount"></td>
		</tr>




		<tr>
			<td>Option 2<input type="checkbox" name="savingsOption"></td>
			<td><label for="Savings"> Savings </label></td>

			<td>Amount <input type="text" name="savingsAmount">
			</td>
		</tr>




		<tr>
			<td>Option 3<input type="checkbox" name="rewardsOption"></td>
			<td><label for="Rewards"> Rewards </label></td>

			<td>Amount <input type="text" name="rewardsAmount">
			
			</td>
		</tr>
		<tr><td colspan="3"><br></td></tr>
		


		<tr>
			<td>Option 4<input type="checkbox" name="creditOption"></td>
			<td colspan="2"><label for="Credit Card">Credit Card
					Information: </label></td>
		</tr>
		<tr>
			<td>Enter Card No.</td>

			<td colspan="2"><input type="text" name="cardNo"></td>
		</tr>

		<tr>
			<td>CVV2</td>
			<td colspan="2"><input type="text" name="cvv2"></td>
		</tr>
		<tr>
			<td>Amount</td>
			<td colspan="2"><input type="text" name="creditAmount">
			</td>
		</tr>







		<tr>
			<td colspan="3" align="right"><label> Your Order Total
					is: $<%=(float) cart.getItemTotalCost()%></label></td>
		</tr>
		<tr>
			<td colspan="3" align="right"><input type="submit" name="submit"
				value="Place Order"></td>
		</tr>
		</td>
		</tr>
	</table>

</form>
<%@include file="templates/footer.jsp"%>


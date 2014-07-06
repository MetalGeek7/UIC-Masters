<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="cs442.group2.BankingApplication.Item"%>
<%@page import="java.util.List"%>
<%@page import="cs442.group2.BankingApplication.Cart"%>
<%@page import="cs442.group2.BankingApplication.Customer"%>
<%
	String userName = (String) session.getAttribute("userName");
	String password = (String) session.getAttribute("password");
	
	//String userName = "akanch4";
	//String password = "password";
	
	// Use session logic below
	Customer customer = Customer.authenticate(userName, password);
	if (customer == null) {
		//System.out.println("InValid login");
		session.setAttribute("message", "Please Login");
		response.sendRedirect(response.encodeRedirectURL("login.jsp"));
	} else {
	Cart cart = customer.getCart();

	List<Item> items = cart.getAllItems();
	
	int itemID1 = 1;
	/*String itemName1 = "The Hobbit";
	int itemQuantity1 = 1;
	double itemCost1 = 21.45 ; */

	int itemID2 = 2;
	/*String itemName2 = "The Fellowship of the Ring(The Lord of the Rings #1)";
	int itemQuantity2 = 1;
	double itemCost2 = 22.43 ; */

	/*Item item1 = new Item(itemID1);
	Item item2 = new Item(itemID2);

	cart.addItem(item1, 1);
	cart.addItem(item2, 1);
	*/
	String submitValue = request.getParameter("submit");
	//System.out.println("Value of submit button pressed: "+submitValue);
	if (submitValue != null) {
		if (submitValue.equals("Update Cart")) {
			List<String> params = new ArrayList<String>(request
					.getParameterMap().keySet());
			//System.out.println("Update to Cart called ");
			for (String param : params) {
				if (param.startsWith("quantity")) {
					
					
					int qty = Integer.parseInt(request.getParameter(param));
					//System.out.println("method invoked successfullly with quantity:"+qty);
					String paramValue1 = param.substring(8);
					int itemid1 = Integer.parseInt(paramValue1);
					Item itemOne = new Item(itemid1);
					cart.changeQuantityOfItemInCart(itemOne, qty);
					
				}
			}

			for (String param : params) {
				if (param.startsWith("checked")) {
					if(request.getParameter(param).equals("on")){
					String paramValue2 = param.substring(7);
					int itemid2 = Integer.parseInt(paramValue2);
					Item itemTwo = new Item(itemid2);
					cart.removeItem(itemTwo);
				}
			}

		}
	}
	}
 	items = cart.getAllItems();
	//System.out.println("Items recvd: "+items.size());
	//else if (submitValue.equals("Proceed to Checkout")) {

	// check if strings in params start with checked...

	// After updating cart,
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

	<table border="1" cellpadding="2" style="width: 100%">
	<form action="ViewCart.jsp" method="post">
		<tr>
			<th>Delete Product</th>
			<th>Name</th>
			<th>Cost</th>
			<th>Quantity</th>
		</tr>
		<%
			for (Item item : items) {
		%>

		<tr>
			<td><input type="checkbox" name="checked<%=item.getItemID()%>">
			</td>
			<td><%=item.getItemName()%></td>
			<td><%=item.getItemCost()%></td>
			<td><input type="number" name="quantity<%=item.getItemID()%>"
				value="<%=cart.getQuantityOfItem(item) %>"></td>
		</tr>
		
		<%
			}
		%>
		

		<tr>
			<td colspan="4" align="right">
					<input type="submit" name="submit" value="Update Cart">

			</td>
</form>
			<td colspan="4" align="right">
				<form action="Checkout.jsp" method="post">
					<label> <input type="submit" name="Submit"
						value="Proceed to Checkout" />
					</label>
				</form>
		</tr>
	</table>

<%@include file="templates/footer.jsp"%>
<%
	}
%>

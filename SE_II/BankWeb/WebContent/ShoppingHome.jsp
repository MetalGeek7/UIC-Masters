<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="cs442.group2.BankingApplication.Item"%>
<%@page import="java.util.List"%>
<%@page import="cs442.group2.BankingApplication.Cart"%>
<%@page import="cs442.group2.BankingApplication.Customer"%>

<%
	String userName = (String) session.getAttribute("userName");
	String password = (String) session.getAttribute("password");
	Cart custCart = null;
	int checkindex = 0;

	Customer customer = Customer.authenticate(userName, password);
	if (customer == null) {
		//System.out.println("InValid login");
		session.setAttribute("message", "Please Login");
		response.sendRedirect(response.encodeRedirectURL("login.jsp"));
	} else {
		//Valid Login --> render the html
		//System.out.println("Valid login");
	custCart = customer.getCart();
	String submitValue = request.getParameter("submit");
	List<Item> items = null;
	if (submitValue == null) {
		items = Item.getAllItems();
	} else{
		if (submitValue.equals("Search")) {
			items = Item.searchItem(request.getParameter("itemName"));
		} 
		else if (submitValue.equals("Add To Cart")) {
			items = Item.getAllItems(); // Items must always be non null
			
			// Update cart of user
			int qty = 0;
			String paramItemID;
			Item checkedItem;
			
			List<String> params = new ArrayList<String>(request.getParameterMap().keySet());
			for (String param : params) {
				if (param.startsWith("checked")) {
						paramItemID = param.substring(7);
						int itemID = Integer.parseInt(paramItemID);
						//System.out.println("ItemID of item checked = "+ itemID);

						//Get the qty of the checked item corresponding to the retrieved itemID
						for (String param2 : params) {
							if (param2.equals("quantity" + itemID)) {
								//System.out.println("Value of quantity param:"+ param2);
								qty = Integer.parseInt(request.getParameter(param2));
								//System.out.println("Quantity of item checked = "+ qty);
							}
						}

						//Now add this item with its quantity to the customer's cart	
						checkedItem = new Item(itemID);
						custCart.addItem(checkedItem, qty);
						//System.out.println("Item added to cart :" + itemID);

				}
			}
			// After updating cart, 
		}	
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
<form action="ShoppingHome.jsp" method="get">
	<table>
		<tr>
			<td>Product Name</td>
			<td><input type="text" name="itemName"> <input
				type="submit" name="submit" value="Search"></td>

		</tr>
	</table>
</form>
<hr>
<form action="ShoppingHome.jsp" method="post">
	<table  border="1" cellpadding="2" style="width: 100%">
		<tr>
			<th>Select Product</th>
			<th>Name</th>
			<th>Cost</th>
			<th>Quantity</th>
		</tr>
		<%
			for (Item item : items) {
		%>

		<tr>
			<td>
				<input type="checkbox" name="checked<%=item.getItemID()%>" >
			</td>
			<td>
				<%=item.getItemName()%>
			</td>
			<td>
				<%=item.getItemCost()%>
			</td>
			<td>
				<input type="number" name="quantity<%=item.getItemID()%>" value="1" min="0">
			</td>
		</tr>

		<%
			}
		%>
		<tr>
			<td colspan="4" align="right">
				<input type="submit" name="submit" value="Add To Cart">
			</td>
		</tr>
	</table>
</form>

<%@include file="templates/footer.jsp"%>
<%
	}
%>
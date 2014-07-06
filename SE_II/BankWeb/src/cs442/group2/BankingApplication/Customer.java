package cs442.group2.BankingApplication;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cs442.group2.BankingApplication.annotations.DBAnnotation;
import cs442.group2.BankingApplication.exceptions.InsufficientBalanceException;
import cs442.group2.BankingApplication.exceptions.InvalidCustomerException;
import cs442.group2.BankingApplication.exceptions.ItemNotAvailableException;
import cs442.group2.BankingApplication.exceptions.RewardsCustomerException;
import cs442.group2.BankingApplication.helpers.AccountChoice;

public class Customer {

	private int customerID;
	private int branchID;
	private String userName;
	private List<Account> accounts;
	private Cart cart;

	@Override
	public String toString() {
		String str = "[" + getUserName() + "(" + customerID + ")] Accounts: (\n";
		str += Arrays.toString(accounts.toArray(new Account[0]));
		str += ")";
		return str;
	}

	// Constructor
	private Customer(int customerID, int branchID, String userName,
			List<Account> accounts, Cart cart) {
		this.customerID = customerID;
		this.branchID = branchID;
		this.userName = userName;
		this.accounts = accounts;
		this.cart = cart;
	}

	// Used in login to authenticate the customer
	public static Customer authenticate(String userName, String password) {
		@DBAnnotation(
				variable = {"userName", "password"},
				column   = {"username", "password"},
				table    = {"Customer", "Customer"},
				isSource = {true      , true      }				
		)
		
		
		Customer customer = null;

		Connection conn = BankConnect.getConnection();
		try {
			PreparedStatement statement = conn.prepareStatement(customerLogin);
			statement.setString(1, userName);
			statement.setString(2, password);
			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				ArrayList<Account> accounts = new ArrayList<Account>();

				int customerID = rs.getInt(1);
				int branchID = rs.getInt(2);

				for (Account account : Account.getAllAccounts(customerID)) {
					accounts.add(account);
				}

				Cart cart = new Cart(customerID);
				customer = new Customer(customerID, branchID, userName,
						accounts, cart);
			}

		} catch (Exception e) {
			Reporting.err.println(e);
		}

		return customer;
	}

	// Adds the selected item to the cart
	public boolean addItemToCart(Item item, int quantity) {
		try {
			cart.addItem(item, quantity);
		} catch (Exception e) {

			return false;
		}
		return true;
	}

	// Used to transfer money between two accounts 
	public String transfer(Account fromAccount, Account toAccount, double amount) {
		
		String result = "";
		StringBuilder builder = new StringBuilder();
		Transaction transaction = null;
		try {
			//BankConnect.getConnection().rollback();

			try {
				transaction = Transaction.makeTransaction(this,
						new AccountChoice(fromAccount, amount), toAccount);
				builder.append("Success\n");
				builder.append(transaction.getTransactionID());
				
				

			} catch (InsufficientBalanceException e) {
				builder.append("Failure\n");
				builder.append("InsufficientBalanceException\n");
				builder.append(e.toString());
				BankConnect.getConnection().rollback();

			} catch (RewardsCustomerException e) {
				builder.append("Failure\n");
				builder.append("RewardsCustomerException\n");
				builder.append(e.toString());
				BankConnect.getConnection().rollback();

			} catch (InvalidCustomerException e) {
				builder.append("Failure\n");
				builder.append("InvalidCustomerException\n");
				builder.append(e.toString());
				BankConnect.getConnection().rollback();

			} catch (ItemNotAvailableException e) {
				builder.append("Failure\n");
				builder.append("ItemNotAvailableException\n");
				builder.append(e.toString());
				BankConnect.getConnection().rollback();

			} catch (SQLException e) {
				builder.append("Failure\n");
				builder.append("SQLException\n");

				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				e.printStackTrace(printWriter);

				builder.append(stringWriter.toString());
				BankConnect.getConnection().rollback();
			} catch (Exception e) {
				builder.append("Failure\n");
				builder.append("SQLException\n");

				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				e.printStackTrace(printWriter);
				builder.append(stringWriter.toString());
				BankConnect.getConnection().rollback();
			}

			if (transaction != null) {
				// Transfer was successfull
				BankConnect.getConnection().commit();
			}
		} catch (Exception e) {
			// Error which commit or rollback, rare
			Reporting.err.println(e);
			builder.append("Failure\n");
			builder.append("Exception\n");

			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			builder.append(stringWriter.toString());
			
		}

		result = builder.toString();
		return result;
	}

	// Place and order using multiple accounts to pay for the order
	public String order(List<AccountChoice> fromAccountChoices) {
		Order order = null;
		String result = "";

		StringBuilder builder = new StringBuilder();

		try {
			order = Transaction.makeTransaction(this, fromAccountChoices,
					this.cart);
			if(order == null){
				Reporting.out.println("Order was null");
				throw new Exception("Total money from account choices not sufficient");
			}
			builder.append("Success\n");
			builder.append(order.getOrderID());

		} catch (InsufficientBalanceException e) {
			builder.append("Failure\n");
			builder.append("InsufficientBalanceException\n");

			// toString is overrided for this class, awesome output! :)
			builder.append(e.toString());
		} catch (RewardsCustomerException e) {
			builder.append("Failure\n");
			builder.append("RewardsCustomerException\n");

			// toString is overrided for this class, awesome output! :)
			builder.append(e.toString());
		} catch (InvalidCustomerException e) {
			builder.append("Failure\n");
			builder.append("InvalidCustomerException\n");

			// toString is overrided for this class, awesome output! :)
			builder.append(e.toString());
		} catch (ItemNotAvailableException e) {
			builder.append("Failure\n");
			builder.append("ItemNotAvailableException\n");

			// toString is overrided for this class, awesome output! :)
			builder.append(e.toString());

		} catch (SQLException e) {
			builder.append("Failure\n");
			builder.append("SQLException\n");

			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);

			builder.append(stringWriter.toString());
		} catch (Exception e) {
			builder.append("Failure\n");
			builder.append("Exception\n");

			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);

			builder.append(stringWriter.toString());
		}

		result = builder.toString();

		return result;
	}

	// Returs a list of orders of the customer
	public List<Order> orderHistory() {
		@DBAnnotation(
			variable = { "customerID", "orderID", "fromAccountID"      , "transactionID"       , "dateTime"           , "totalAmount"  , "amountPaid"  , "itemID", "quantityPurchased" },
			column   = { "customerid", "orderid", "fromaccountid"      , "transactionid"       , "shoppingtimestamp"  , "orderpayment" , "amountpaid"  , "itemid", "quantity"          },
			table    = { "Customer"  , "Order"  , "CustomerTransaction", "CustomerTransaction" , "ShoppingOrder"      , "ShoppingOrder", "OrderPayment", "Item"  , "OrderDetails"      },
			isSource = { false       , true     ,  true                , true                  , true                 , true           , true          , true    , true                }
		)
		
		ArrayList<Order> orders = new ArrayList<Order>();
		HashMap<Integer, ArrayList<OrderPayment>> payments = new HashMap<Integer, ArrayList<OrderPayment>>();
		HashMap<Integer, Date> orderIDtoShoppingTimeStamp = new HashMap<Integer, Date>();
		HashMap<Integer, Double> orderIDtoTotalPayment = new HashMap<Integer, Double>();

		String sqlOrderHistory = "SELECT * FROM shoppingorder NATURAL JOIN orderpayment " +
				"NATURAL JOIN customertransaction " +
				" WHERE customerid = ? order by shoppingtimestamp desc;";

		try {
			Connection conn = BankConnect.getConnection();
			PreparedStatement statement = conn
					.prepareStatement(sqlOrderHistory);
			statement.setInt(1, customerID);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {

				int orderID = rs.getInt("orderid");
				int fromAccountID = rs.getInt("fromaccountid");

				int transactionID = rs.getInt("transactionid");
				
				// Date dateTime = rs.getDate("shoppingtimestamp");
				Date dateTime = rs.getTimestamp("shoppingtimestamp");
				orderIDtoShoppingTimeStamp.put(orderID, dateTime);

				double totalAmount = rs.getDouble("orderpayment");
				orderIDtoTotalPayment.put(orderID, totalAmount);

				double amountPaid = rs.getDouble("amountpaid");

				OrderPayment payment = new OrderPayment(orderID, fromAccountID,
						transactionID, amountPaid);

				if (!payments.containsKey(orderID)) {
					ArrayList<OrderPayment> orderPayments = new ArrayList<OrderPayment>();
					orderPayments.add(payment);
					payments.put(orderID, orderPayments);
				} else {
					ArrayList<OrderPayment> orderPayments = payments
							.get(orderID);
					orderPayments.add(payment);
					payments.put(orderID, orderPayments);
				}
			}

			Set<Integer> orderIDs = payments.keySet();
			for (int orderID : orderIDs) {
				ArrayList<OrderPayment> orderPayments = payments.get(orderID);
				List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();

				// get order details of every orderid
				try {
					statement = conn
							.prepareStatement("SELECT * FROM OrderDetails WHERE orderid = ? ;");
					statement.setInt(1, orderID);
					rs = statement.executeQuery();

					while (rs.next()) {
						int itemID = rs.getInt("itemid");
						int quantityPurchased = rs.getInt("quantity");
						orderDetails.add(new OrderDetail(new Item(itemID),
								quantityPurchased));
					}

				} catch (Exception e) {
					Reporting.err.println(e);
				}

				Order order = new Order(orderID, this.getCustomerID(),
						orderIDtoShoppingTimeStamp.get(orderID), orderPayments,
						orderDetails, orderIDtoTotalPayment.get(orderID));
				orders.add(order);
			}

		} catch (Exception e) {
			Reporting.err.println(e);
		}

		return orders;
	}

	/****************************************
	 *			Getters of the Class		*
	 ****************************************/
	public int getCustomerID() {
		return customerID;
	}

	public String getUserName() {
		return userName;
	}

	public Cart getCart() {
		return cart;
	}

	public List<Account> getAccounts() {
		return accounts;
	}
	
	private static String customerLogin = "SELECT customerID, branchID FROM Customer NATURAL JOIN Authentication WHERE userName = ? AND password = md5(?);";

}

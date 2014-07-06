package cs442.group2.BankingApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cs442.group2.BankingApplication.annotations.DBAnnotation;
import cs442.group2.BankingApplication.exceptions.InsufficientBalanceException;
import cs442.group2.BankingApplication.exceptions.InvalidCustomerException;
import cs442.group2.BankingApplication.exceptions.ItemNotAvailableException;
import cs442.group2.BankingApplication.exceptions.RewardsCustomerException;
import cs442.group2.BankingApplication.helpers.AccountChoice;

@SuppressWarnings("unused")
public class Transaction {

	private static String sqlEmptyUserCart = "DELETE FROM Cart WHERE customerID = ?;";
	private int transactionID;
	private int customerID;
	private int fromAccountID;
	private int toAccountID;
	private double amount;
	private Date timestamp;

	@Override
	public String toString() {
		String s = "";
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		s = format.format(timestamp);
		return String
				.format("Transaction(id=%d) From(id=%d) To(id=%d) With Amount(%4.2f) [%s]",
						transactionID, fromAccountID, toAccountID, amount, s);
	}

	private static java.sql.Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}

	// Makes transactions from multiple accounts used during order payment.
	public static Order makeTransaction(Customer customer,
			List<AccountChoice> fromAccountChoices, Cart cart)
			throws InsufficientBalanceException, RewardsCustomerException, ItemNotAvailableException,
			InvalidCustomerException, SQLException, Exception {
		
		@DBAnnotation(
			variable = { " "            , "totalFromAccounts", "autogenerateOrderID", "autogenerateOrderID", " "            , " "           , "autogenerateOrderID", " "           , " "            },
			column   = { "customerid"   , "orderpayment"     , "orderid"            , "orderid"            , "transactionid", "amountpaid"  , "orderid"            , "itemid"      , "quantity"     },
			table    = { "shoppingorder", "shoppingorder"    , "shoppingorder"      , "orderpayment"       , "orderpayment" , "orderpayment", "orderdetails"       , "orderdetails", "orderdetails" },
			isSource = { false          , false              , true                 , false                , false          , false         , false                , false         , false          }
		)
		
		
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		// fromAccountChoices contains list of AccountChoice objects where each
		// object will give you account and the amount you want to deduct from
		// that account

		// These are the problems that got introduced by our design

		// 1. Make sure if there are two accounts that are same, then its
		// replaced by 1 account with the sum of the amounts. Otherwise, what
		// would happen is say there are two choices, deduct 100 from account a1
		// & deduct 100 from account a2. And if account balance is 150 then
		// checkTransaction will be OK for the choices(obviously) but the
		// insert will fail

		// Note: you can check if two accounts are same by just doing,
		// account1 == account2
		// where account1 & account 2 are objects of type Account. This is
		// possible because comparesTo function is overridden in Account class.

		// Create orderpayments and add to this
		List<OrderPayment> orderPayments = new ArrayList<OrderPayment>();
		List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();

		Order order = null;

		boolean error = false;

		if (customer.getCustomerID() == 1)
			error |= true;

		List<Item> items = cart.getAllItems();

		double totalFromCart = cart.getItemTotalCost();

		// Remove duplicate accounts
		HashMap<Integer, AccountChoice> choices = new HashMap<Integer, AccountChoice>();
		for (AccountChoice choice : fromAccountChoices) {

			if (choices.get(choice.getAccount().getAccountID()) == null) {
				choices.put(choice.getAccount().getAccountID(), choice);

			} else {
				AccountChoice existingchoice = choices.get(choice.getAccount()
						.getAccountID());
				existingchoice.setDeductAmount(existingchoice.getDeductAmount()
						+ choice.getDeductAmount());
				choices.put(choice.getAccount().getAccountID(), existingchoice);
			}

		}
		// Duplicates removed

		double totalFromAccounts = 0.0d;
		for (AccountChoice choice : choices.values()) {
			totalFromAccounts += choice.getDeductAmount();
		}

		if (totalFromAccounts != totalFromCart){
			error |= true;
		}

		for (AccountChoice choice : choices.values()) {
			try {
				Transaction.checkTransaction(customer, choice);
			} catch (Exception e) {
				Reporting.err.println(e);
				error |= true;
				throw e;
			}
		}

		if (!error) {
			// Proceed with the order

			try {
				Connection conn = BankConnect.getConnection();

				// STEP 1: Make a transaction in customertransaction where
				// toAccount
				// is Portal account
				Account portalAcc = Account.getAccount(1); // In table Account,
															// 1 is always for
															// Portal user's
															// account
				ArrayList<Transaction> transactionlist = new ArrayList<Transaction>();
				int autogenerateOrderID = 0;

				for (AccountChoice choice : choices.values()) {
					// For each unique account chosen by customer for his
					// payment
					// make a transaction in customertransaction table
					Transaction transaction = Transaction.makeTransaction(
							customer, choice, portalAcc);

					transactionlist.add(transaction);
				}

				// STEP 2: Create an Order object in shoppingorder table
				String SQLShoppingorderInsert = "INSERT INTO shoppingorder (customerid,orderpayment) VALUES (?,?);";
				String autokey[] = { "orderid" };
				try {
					PreparedStatement statement = conn.prepareStatement(
							SQLShoppingorderInsert, autokey);
					statement.setInt(1, customer.getCustomerID());
					statement.setDouble(2, totalFromAccounts);

					statement.executeUpdate();

					ResultSet rs = statement.getGeneratedKeys();

					if (rs.next()) {
						autogenerateOrderID = rs.getInt(1);

					}

					// STEP 3: Make rows in orderpayment table
					String SQLOrderpaymentInsert = "INSERT INTO orderpayment (orderid,transactionid,amountpaid) VALUES (?,?,?);";
					for (Transaction transaction : transactionlist) {
						PreparedStatement stmt = conn
								.prepareStatement(SQLOrderpaymentInsert);
						stmt.setInt(1, autogenerateOrderID);
						stmt.setInt(2, transaction.getTransactionID());
						stmt.setDouble(3, transaction.getAmount());
						stmt.executeUpdate();

						OrderPayment orderpaymt = new OrderPayment(
								autogenerateOrderID,
								transaction.getFromAccountID(),
								transaction.getTransactionID(),
								transaction.getAmount());
						orderPayments.add(orderpaymt);

					}

					// STEP 4: Insert record in orderdetails table
					String SQLOrderdetailsInsert = "INSERT INTO orderdetails (orderid,itemid,quantity) VALUES (?,?,?);";
					for (Item item : cart.getAllItems()) {
						PreparedStatement stmt = conn
								.prepareStatement(SQLOrderdetailsInsert);
						stmt.setInt(1, autogenerateOrderID);
						stmt.setInt(2, item.getItemID());
						stmt.setInt(3, cart.getQuantityOfItem(item));
						stmt.executeUpdate();
					}

					// STEP 5: Delete all items in Cart table for the customer
					// AKSHAY: Uncomment step 5 after changing OrderStatus display
					statement = conn.prepareStatement(sqlEmptyUserCart);
					statement.setInt(1, customer.getCustomerID());
					statement.executeUpdate();

					// STEP 6: Update quantity of Items purchased by customer.

					for (Item item : items) {
						// Integer passed to setQuantity is actual a decrement
						// value, its not the value which is set in an
						// "absolute" way
						// System.out.println("Subtracting this from quantity: " + cart.getQuantityOfItem(item));
						Item.setQuantity(item.getItemID(),
								cart.getQuantityOfItem(item));
					}

					conn.commit(); // Save everything in every table involved
									// above and makeTransaction for transfering
									// amount

					// Now do orderdetails
					for (Item item : cart.getAllItems()) {
						OrderDetail detail = new OrderDetail(new Item(
								item.getItemID()), // Better to update Item
													// since quantity changed
								cart.getQuantityOfItem(item));
						orderDetails.add(detail);
					}

					// Clear the cart

					cart.updateCart();

					order = new Order(autogenerateOrderID,
							customer.getCustomerID(), new Date(),
							orderPayments, orderDetails, totalFromAccounts);

				} catch (SQLException e) {
					Reporting.err.println(e);
					conn.rollback();
					throw e;
				}

			} catch (Exception e) {
				Reporting.err.println(e);
				throw e;
			}

		}

		return order; // You can return null from this function if order fails
	}

	// Makes transactions from a single account used during transfer of
	// amount between two accounts.
	public static Transaction makeTransaction(Customer customer,
			AccountChoice fromAccountChoice, Account toAccount)
			throws InsufficientBalanceException, RewardsCustomerException,
			InvalidCustomerException, SQLException, Exception {

		@DBAnnotation(
			variable = { "deductionAmount", "fromAccountID", "customerID", "toRecepientAccountID", "customerID"         , "fromAccountID"      , "toRecepientAccountID", "deductionAmount"    , "generatedTransactionID", "rewardsAmountToGive" },
			column   = { "balance"        , "accountid"    , "customerid", "accountid"           , "customerid"         , "fromAccountid"      , "toaccountid"         , "amount"             , "transactionid"         , "balance"             },
			table    = { "Account"        , "Account"      , "Account"   , "Account"             , "CustomerTransaction", "CustomerTransaction", "CustomerTransaction" , "CustomerTransaction", "CustomerTransaction"   , "Account"             },
			isSource = { false            , false          , false       , false                 , false                , false                , false                 , false                , true                    , false                 }
		)

		
		Transaction transaction = null;
		// This will contain only 1
		// ArrayList<Transaction> transactions = new ArrayList<Transaction>();

		// STEP 1: Check whether the transaction is possible i.e. whether the
		// 'From'
		// account of the customer has enough balance to make a successful
		// transfer
		boolean isValid = false;
		try {
			Transaction.checkTransaction(customer, fromAccountChoice);
			isValid = true;
		} catch (Exception e) {
			Reporting.err.println(e);
			throw e;
		}

		// STEP 2: Deduct money from Sender's account, add it to recepient's
		// account and then record the
		// transaction in the CustomerTransaction table in the database.
		if (isValid = true) {

			Connection conn = BankConnect.getConnection();

			int customerID = customer.getCustomerID();

			Account account = fromAccountChoice.getAccount();
			int fromAccountID = account.getAccountID();
			int toRecepientAccountID = toAccount.getAccountID();
			double deductionAmount = fromAccountChoice.getDeductAmount();

			String SQLAccountUpdateFromAccount = "UPDATE account SET balance= balance - ?  WHERE accountid = ? and customerid = ?;";
			String SQLAccountUpdateToAccount = "UPDATE account SET balance= balance + ?  WHERE accountid = ? and customerid = ?;";
			String SQLTransactionInsert = "INSERT INTO customertransaction (customerid,fromaccountid,toaccountid,amount) VALUES (?,?,?,?);";
			String SQLTransactionSelect = "SELECT transactionid, customerid,fromaccountid, toaccountid, amount,timestamp FROM customertransaction WHERE transactionid = ? ;";

			try {
				try {

					if (conn != null) {

						// Deducts money from the sender's account in Account
						// table
						PreparedStatement statement = conn
								.prepareStatement(SQLAccountUpdateFromAccount);

						statement.setDouble(1, deductionAmount);
						statement.setInt(2, fromAccountID);
						statement.setInt(3, customerID);
						statement.executeUpdate();

						// TODO: Rewards for customer with fromAccount if its a
						// credit account

						// Adds money to the recepient's account in Account
						// table
						statement = conn
								.prepareStatement(SQLAccountUpdateToAccount);

						statement.setDouble(1, deductionAmount);
						statement.setInt(2, toRecepientAccountID);
						statement.setInt(3, customerID);
						statement.executeUpdate();

						// Creates a transaction in the CustomerTransaction
						// table

						String key[] = { "transactionid" };
						statement = conn.prepareStatement(SQLTransactionInsert,
								key);

						statement.setInt(1, customerID);
						statement.setInt(2, fromAccountID);
						statement.setInt(3, toRecepientAccountID);
						statement.setDouble(4, deductionAmount);
						statement.executeUpdate();

						ResultSet rs = statement.getGeneratedKeys();
						int generatedTransactionID = 0;
						// To get the database auto-generated transactionid of
						// the transaction just inserted
						if (rs.next()) {
							generatedTransactionID = rs.getInt(1);

						}

						// To create the transaction object that would be
						// returned to the calling class.

						statement = conn.prepareStatement(SQLTransactionSelect);

						statement.setInt(1, generatedTransactionID);
						rs = statement.executeQuery();

						if (rs.next()) {
							int transactionid = rs.getInt("transactionid");
							int customerid = rs.getInt("customerid");
							int fromaccountid = rs.getInt("fromaccountid");
							int toaccountid = rs.getInt("toaccountid");
							Double amount = rs.getDouble("amount");
							Date timestamp = rs.getDate("timestamp");

							transaction = new Transaction(transactionid,
									customerid, fromaccountid, toaccountid,
									amount, timestamp);


							if (fromAccountChoice.getAccount().getAccountType().equalsIgnoreCase("Credit")) {
								
								// Add rewards for the credit card user
								double rewards_percentage = 0.1;
								
								double rewardsAmountToGive = fromAccountChoice
										.getDeductAmount() * rewards_percentage;

								// Reporting.out.println("Giving rewards : " + rewardsAmountToGive);
								String sqlGiveRewards = "UPDATE Account set balance = balance + ? "
									+ "where accountid in " + 
									"(select accountid from Account NATURAL JOIN AccountType where accounttypename = 'Rewards' " +
									"and customerid in (select customerid from Account where accountid = ?) limit 1);";
								PreparedStatement stGiveRewards = conn
										.prepareStatement(sqlGiveRewards);
								stGiveRewards.setDouble(1, rewardsAmountToGive);
								stGiveRewards.setInt(2, fromAccountChoice.getAccount().getAccountID());
								stGiveRewards.executeUpdate();

							}
						}
					}
				} catch (SQLException e) {
					throw e;
				}

			} catch (Exception e) {
				Reporting.err.println(e);
				throw e;
			}

			// RETURNS the transaction object to the caller
			return transaction;

		} else {
			return null; // IF transaction isValid = False
		}

	}

	// To check if a transaction can take place, used by both makeTransactions() functions 
	public static void checkTransaction(Customer customer,
			AccountChoice accountChoice) throws InsufficientBalanceException,
			InvalidCustomerException, RewardsCustomerException {

		// We have introduced these problems which we need to handle

		// 1. Make sure if payment account is a rewards account, it should be of
		// the customer only.
		// i.e. a person cannot add some other customer's reward account as a
		// payment option
		if (customer.getCustomerID() == 1
				|| accountChoice.getAccount().getAccountID() == 1) { // Account
																		// OR
																		// Customer
																		// belong
																		// to
																		// the
																		// Portal

			// This exception is only for portal user
			InvalidCustomerException exceptionInvalidCustomer = new InvalidCustomerException(
					customer);
			Reporting.err.println(exceptionInvalidCustomer);
			throw exceptionInvalidCustomer;
		}

		Account select_acc = accountChoice.getAccount();
		if (select_acc.getAccountType().equals("Rewards")
				&& select_acc.getCustomerID() != customer.getCustomerID()) {
			// Rewards account does not belong to current logged in customer

			RewardsCustomerException exceptionRewards = new RewardsCustomerException(
					select_acc, customer);
			Reporting.err.println(exceptionRewards);
			throw exceptionRewards;
		} else {

			double availbalance = accountChoice.getAccount().getBalance();
			if (availbalance < accountChoice.getDeductAmount()) { // finally check if there is sufficient balance

				InsufficientBalanceException exceptionLessBalance = new InsufficientBalanceException(
						accountChoice.getAccount(),
						accountChoice.getDeductAmount());
				Reporting.err.println(exceptionLessBalance);
				throw exceptionLessBalance;

			}

		}

	}

	// Constructor
	Transaction(int transactionID, int customerID, int fromAccountID,
			int toAccountID, double amount, Date timestamp) {
		this.transactionID = transactionID;
		this.customerID = customerID;
		this.fromAccountID = fromAccountID;
		this.toAccountID = toAccountID;
		this.amount = amount;
		this.timestamp = timestamp;
	}

	/****************************************
	 *			Getters of the Class		*
	 ****************************************/
	public int getTransactionID() {
		return transactionID;
	}

	public int getCustomerID() {
		return customerID;
	}

	public int getFromAccountID() {
		return fromAccountID;
	}

	public int getToAccountID() {
		return toAccountID;
	}

	public double getAmount() {
		return amount;
	}

	public Date getTimestamp() {
		return timestamp;
	}

}

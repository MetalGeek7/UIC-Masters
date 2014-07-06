package cs442.group2.BankingApplication;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs442.group2.BankingApplication.annotations.DBAnnotation;

public class Account implements Comparable<Account> {
	private static String sqlGetCreditCardByNumber = "SELECT * FROM Account NATURAL JOIN Credit NATURAL JOIN AccountType WHERE cardNumber = ? AND cvv2 = ?;";
	private int accountID; 
	private int customerID;
	private String accountType;
	private int pin;

	@Override
	public String toString() {
		String str = String.format("Account(accountID=%d) : %s : %f\n", accountID, accountType,
				getBalance());
		return str;
	}

	// Constructor
	protected Account(int accountID, int customerID, String accountType, int pin) {
		this.accountID = accountID;
		this.customerID = customerID;
		this.accountType = accountType;
		this.pin = pin;
	}
	
	// Returns the account information for an input of creditcard number and the cvv number
	public static Account getAccountByCreditCardNumber(String cardNumber,
			String cvv2) {
		@DBAnnotation(
			variable = { "accountID", "customerID", "accountType"    , "pin    ", "approvedCredit" },
			column   = { "accountid", "customerid", "accounttypename", "pin    ", "approvedcredit" },
			table    = { "Account"  , "Account"   , "AccountType"    , "Account", "Account"        },
			isSource = { true       , true        , true             , true     , true             }
		)
		
		Account account = null;
		try {
			Connection conn = BankConnect.getConnection();
			PreparedStatement statement = conn
					.prepareStatement(sqlGetCreditCardByNumber);
			statement.setString(1, cardNumber);
			statement.setString(2, cvv2);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				int accountID = rs.getInt("accountid");
				int customerID = rs.getInt("customerid");
				String accountType = rs.getString("accounttypename");
				int pin = rs.getInt("pin");
				double approvedCredit = rs.getDouble("approvedcredit");
				account = new Credit(accountID, customerID, accountType, pin,
						cardNumber, cvv2, approvedCredit);
			}

		} catch (Exception e) {
			Reporting.err.println(e);
		}
		return account;
	}

	// Given an account number, this function retrieves the account information
	public static Account getAccount(int accountID) {
		@DBAnnotation(
			variable = { "accountID", "accType"      , " "         , " "              , " "      , " "         , " "     , " "              },
			column   = { "accountid", "accountTypeID", "customerID", "accountTypeName", "pin"    , "cardNumber", "cvv2"  , "approvedCredit" },
			table    = { "Account"  , "AccountType"  , "Account"   , "AccountType"    , "Account", "Credit"    , "Credit", "Credit"         },
			isSource = { false      , true           , true        , true             , true     , true        , true    , true             }
		)
		
		Account account = null;

		Connection conn = BankConnect.getConnection();

		String accID = "SELECT * FROM Account NATURAL JOIN AccountType WHERE accountID = ?;";

		try {
			PreparedStatement statement = conn.prepareStatement(accID);
			statement.setInt(1, accountID);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				int accType = rs.getInt("accountTypeID");

				if (accType == 3) {
					// Its a Credit account

					String selectcredit = "SELECT * FROM Credit NATURAL JOIN Account NATURAL JOIN AccountType WHERE accountID = ?;";
					PreparedStatement stmtcredit = conn
							.prepareStatement(selectcredit);
					stmtcredit.setInt(1, accountID);
					ResultSet rscr = stmtcredit.executeQuery();
					while (rscr.next()) {
						account = new Credit(rscr.getInt("accountID"),
								rscr.getInt("customerID"),
								rscr.getString("accountTypeName"),
								rscr.getInt("pin"),
								rscr.getString("cardNumber"),
								rscr.getString("cvv2"),
								rscr.getFloat("approvedCredit"));
					}

				} else {
					// Its either a Checking, Savings or Rewards account
					account = new Account(rs.getInt("accountID"),
							rs.getInt("customerID"),
							rs.getString("accounttypename"), rs.getInt("pin"));
				}
			}
		} catch (Exception e) {
			Reporting.err.println(e);
		}

		return account; // its OK to return null as account may not exist
	}

	// Retrieves all the accounts for a given customer(based on customer ID)
	public static List<Account> getAllAccounts(int customerID) {
		@DBAnnotation(
			variable = { "customerID", " "        , " "              , " "       },     
			column   = { "customerid", "accountID", "accountTypeName", "pin"     },    
			table    = { "Customer"  , "Account"  , "AccountType"    , "Account" },
			isSource = { false       , true       , true             , true      }
		)
		
		ArrayList<Account> accounts = new ArrayList<Account>();

		Connection conn = BankConnect.getConnection();
		try {

			PreparedStatement statement = conn
					.prepareStatement(customerAccounts);
			statement.setInt(1, customerID);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				Account account = new Account(rs.getInt("accountID"),
						customerID, rs.getString("accountTypeName"),
						rs.getInt("pin"));
				accounts.add(account);
			}

		} catch (SQLException e) {
			Reporting.err.println(e);
		} finally {

		}

		return accounts;
	}

	// Retrieves the account balance information for the account.
	public double getBalance() {
		@DBAnnotation(
			variable = { "balance" },
			column   = { "balance" },
			table    = { "Account" },
			isSource = { true      }
		)
		
		BigDecimal balance = null;
		try {
			Connection conn = BankConnect.getConnection();

			PreparedStatement statement = conn.prepareStatement(accountBalance);
			statement.setInt(1, accountID);

			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				balance = rs.getBigDecimal("balance");
			}
		} catch (Exception e) {
			Reporting.err.println(e);
		}
		if (balance == null)
			return -1;
		return balance.doubleValue();
	}

	// Retrieves a list of all transactions done on an account.
	public List<Transaction> accountHistory() {
		@DBAnnotation(
			variable = { "accountID"          , "accountID"          , " "                  , " "                  , " "                  , " "                  , " "                  , " "                   },
			column   = { "fromAccountID"      , "toAccountID"        , "transactionid"      , "customerid"         , "fromaccountid"      , "toaccountid"        , "amount"             , "timestamp"           },
			table    = { "CustomerTransaction", "CustomerTransaction", "CustomerTransaction", "CustomerTransaction", "CustomerTransaction", "CustomerTransaction", "CustomerTransaction", "CustomerTransaction" },
			isSource = { false                , false                , true                 , true                 , true                 , true                 , true                 ,  true                 }
		)
		
		ArrayList<Transaction> transactions = new ArrayList<Transaction>();

		String sql = "SELECT * FROM CustomerTransaction WHERE fromAccountID = ? OR toAccountID = ? ORDER BY transactionID DESC;";

		try {
			Connection conn = BankConnect.getConnection();

			PreparedStatement statement = conn.prepareStatement(sql);
			// statement.setInt(1, this.getCustomerID());
			statement.setInt(1, this.getAccountID());
			statement.setInt(2, this.getAccountID());
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {

				// We need to change visibility of Transaction constructor to
				// public/protected
				Transaction tr = new Transaction(rs.getInt("transactionid"),
						rs.getInt("customerid"), rs.getInt("fromaccountid"),
						rs.getInt("toaccountid"), rs.getDouble("amount"),
						rs.getTimestamp("timestamp"));
				transactions.add(tr);
			}
		} catch (Exception e) {
			Reporting.err.println(e);
		}
		return transactions;
	}

	// Compares two accounts to check whether they are identical.
	@Override
	public int compareTo(Account o) {
		// Checks if two accounts are identical, do not infer any conclusions
		// when this program returns 1 or -1

		// Useful in Transaction class
		if (this.accountID < o.accountID)
			return -1;
		if (this.accountID > o.accountID)
			return 1;
		if (this.accountID == o.accountID && this.customerID == o.customerID)
			return 0;
		return 1;
	}

	
	/****************************************
	 *	Getters and Setters of the Class	*
	 ****************************************/
	
	public int getAccountID() {
		return accountID;
	}
	
	public int getPin() {
		return pin;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	private static String customerAccounts = "SELECT accountID, accountTypeName, pin FROM Customer NATURAL JOIN Account NATURAL JOIN AccountType WHERE customerID = ?; ";
	private static String accountBalance = "SELECT balance FROM Account WHERE accountID = ?;";

}

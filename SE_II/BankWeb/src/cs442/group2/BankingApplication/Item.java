package cs442.group2.BankingApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import cs442.group2.BankingApplication.annotations.DBAnnotation;
import cs442.group2.BankingApplication.exceptions.ItemNotAvailableException;

@SuppressWarnings("unused")
public class Item implements Comparable<Item>{
	private int itemID;
	private String itemName;
	private int itemQuantity;
	private double itemCost;

	@Override
	public String toString() {
		return String.format("Item(itemID=%d)[%s][qty=%d]", itemID, itemName, getItemQuantity());
	}

	// To set the total available quantity of an item  
	public static void setQuantity(int itemID, int quantity)
			throws ItemNotAvailableException {
		// Add DB Code, to update quantity in Item table
		// This code is only called after an order is succesful

		// Do NOT commit in this code, because Order may produce an error
		@DBAnnotation(
			variable = { "quantity", "itemID" },
			column   = { "quantity", "itemID" },
			table    = { "Item"    , "Item"   },
			isSource = { false     , false    }
		)
		
		String updateTableSQL = "UPDATE item SET quantity = quantity - ?  WHERE itemid = ?";
		try {
			Connection conn = BankConnect.getConnection();
			PreparedStatement statement = conn.prepareStatement(updateTableSQL);
			statement.setInt(1, quantity);
			statement.setInt(2, itemID);
			statement.executeUpdate();

			// DO NOT COMMIT

		} catch (Exception e) {
			// Could be that connection broke, but lets assume that
			// the error occurred only because Item quantity is already 0
			Reporting.err.println(e);
			throw new ItemNotAvailableException(new Item(itemID));
		}

	}

	// Returns a list of items that satisfies the search criteria
	public static List<Item> searchItem(String itemName) {
		@DBAnnotation(
			variable = { "itemName", "itemID", "itemQuantity", "itemCost" },
			column   = { "itemName", "itemID", "quantity"    , "itemCost" },
			table    = { "Item"    , "Item"  , "Item"        , "Item"     },
			isSource = { false     , false   , false         , false      }
		)
		
		ArrayList<Item> result = new ArrayList<Item>();
		// Add DB code.

		String SQLSearchItem = "SELECT * FROM Item WHERE lower(itemName) LIKE '%"
				+ itemName.toLowerCase() + "%'";

		try {
			Connection conn = BankConnect.getConnection();
			try {
				if (conn != null) {
					ResultSet rs = null;
					PreparedStatement statement = conn
							.prepareStatement(SQLSearchItem);
					// statement.setString(1, itemName);
					try {
						rs = statement.executeQuery();
					} catch (SQLException e) {
						System.out.println(e);
					}
					while (rs.next()) {
						// Retrieve by column name
						int itemID = rs.getInt("itemID");
						itemName = rs.getString("itemName");
						int itemQuantity = rs.getInt("quantity");
						double itemCost = rs.getDouble("itemCost");

						Item item = new Item(itemID, itemName, itemQuantity,
								itemCost);
						result.add(item);

					}
				}
			} catch (SQLException e) {
				Reporting.err.println(e);
				conn.rollback();
			}
		} catch (SQLException e) {
			Reporting.err.println(e);
		}

		return result;
	}

	// Constructor
	public Item(int itemID) {
		/*
		@DBAnnotation(
			variable = { "itemID", "itemName", "itemQuantity", "itemCost" },
			column   = { "itemid", "itemname", "quantity"    , "itemcost" },
			table    = { "Item"  , "Item"    , "Item"        , "Item"     },
			isSource = { false   , true      , true          , true       }
		)
		*/
		
		this.itemID = itemID;
		try {
			Connection conn = BankConnect.getConnection();
			if (conn != null) {
				PreparedStatement statement = conn
						.prepareStatement(shoppingItem);
				statement.setInt(1, itemID);
				ResultSet rs = statement.executeQuery();
				if (rs.next()) {
					itemName = rs.getString("itemname");
					itemQuantity = rs.getInt("quantity");
					itemCost = rs.getDouble("itemcost");
				} else {
					throw new RuntimeException("No Item with itemID = "
							+ itemID);
				}
			} else {
				throw new RuntimeException("Connection not established");
			}
		} catch (Exception e) {
			Reporting.err.println(e);
			itemName = "ERROR";
			itemQuantity = -1;
			itemCost = 0;
		}
	}

	private static String shoppingItem = "SELECT * FROM Item WHERE itemID = ? ;";

	// Compare two items to check whether they are identical
	@Override
	public int compareTo(Item o) {
		if(this.getItemID() == o.getItemID())
			return 0;
		
		return (this.getItemID() < o.getItemID()) ? -1 : 1;
	}
	
	/****************************************
	 *			Getters of the Class		*
	 ****************************************/
	
	public String getItemName() {
		return itemName;
	}

	public static List<Item> getAllItems() {
		return searchItem("");
	}
	
	public int getItemID() {
		return itemID;
	}

	public int getItemQuantity() {
		return itemQuantity;
	}

	public double getItemCost() {
		return itemCost;
	}

	// Constructor
	public Item(int itemID, String itemName, int itemQuantity, double itemCost) {
		this.itemID = itemID;
		this.itemName = itemName;
		this.itemQuantity = itemQuantity;
		this.itemCost = itemCost;

	}
}

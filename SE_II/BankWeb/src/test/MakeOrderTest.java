package test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cs442.group2.BankingApplication.*;
import cs442.group2.BankingApplication.helpers.AccountChoice;

@SuppressWarnings({ "unused", "rawtypes", "deprecation" })
public class MakeOrderTest {
	private static String _driverClass = "org.postgresql.Driver";

	// Do not change this
	private static String _jdbcConnection = "jdbc:postgresql://127.0.0.1:5432/group2db";
	private static String database;
	private static String username = "postgres";
	private static String password = "password";

	public Object lockMe = new Object();

	// NOTE:
	// It is not possible to drop a database if there is any connection to it
	// either from the code or using a tool(like pgAdmin3)

	// So, make sure no active connections exists when dropping a database

	static {
		// Give this a unique name for every test
		database = "group2db_order";
	}

	@Test
	public void MakeProperOrder() throws Exception {

		IDatabaseConnection connection = getConnection();
		IDataSet dataset = new FlatXmlDataSet(new FileInputStream(
				"testData/makeOrder.xml")); // Check dbDump.xml for help

		DatabaseOperation.CLEAN_INSERT.execute(connection, dataset);

		// Get the Connection version from IDatabaseConnection
		BankConnect.useConnection(connection.getConnection());

		// Your test code starts
		System.out.println("---");

		ArrayList<Account> listAcc = new ArrayList<Account>();
		Account acc1 = Account.getAccount(6);

		Account acc2 = Account.getAccount(18);

		Customer abhishekKanchan = Customer.authenticate("akanch4", "password");
		Cart cart = abhishekKanchan.getCart();
		

		if (abhishekKanchan.getCart().getAllItems().size() == 0) {
			List<Item> items = Item.getAllItems();
			for (int i = 0; i < 3; i++) {
				abhishekKanchan.addItemToCart(items.get(i), i + 1);
			}
		}

		cart.changeQuantityOfItemInCart(cart.getAllItems().get(0), 5);
		cart.removeItem(cart.getAllItems().get(cart.getAllItems().size()-1));
		// Choose option for payment
		
		System.out.println("Final Cart of user:\n" + cart.toString());

		ArrayList<AccountChoice> accChoices = new ArrayList<AccountChoice>();
		AccountChoice choice1 = new AccountChoice(acc1, abhishekKanchan
				.getCart().getItemTotalCost() - 20);
		AccountChoice choice2 = new AccountChoice(acc2, 20);

		accChoices.add(choice1);
		accChoices.add(choice2);

		String orderstatus = abhishekKanchan.order(accChoices);
		System.out.println("Order Status is : "
				+ Arrays.toString(orderstatus.split("\n")));
		
		System.out.println("Complete Order Information: ");
		System.out.println(abhishekKanchan.orderHistory().get(0));;

		assertEquals("Success", orderstatus.split("\n")[0]);

		// Reporting.out.println("Print something if you want to using this function");
		connection.close(); // Important

	}

	@Before
	public void createSchema() throws Exception {
		/*
		 * This function creates a database with the name in the variable
		 * "database" Also, it will create all the tables necessary for the new
		 * database The values in the tables should be used from xml file
		 * Because, that is the purpose of using DBUnit, to have different
		 * databases for each test, databases that have less values and only
		 * those related to the test
		 * 
		 * For e.g. To test User Login, i have added only values for 1 user
		 */

		Class.forName(_driverClass);
		Connection _connection = DriverManager.getConnection(_jdbcConnection,
				username, password);
		Statement statement = _connection.createStatement();
		String sql = "DROP DATABASE IF EXISTS " + database;
		statement.executeUpdate(sql);

		// Postgres doesn't have `if not exists` for create database command, :(
		sql = "CREATE DATABASE " + database + " OWNER postgres";
		statement.executeUpdate(sql);
		statement.close();
		_connection.close();

		IDatabaseConnection conn = getConnection();
		ScriptRunner runner = new ScriptRunner(conn.getConnection());
		runner.setLogWriter(null);
		runner.setErrorLogWriter(null);
		runner.runScript(new FileReader("testSQL/group2db-ddl.sql"));
		conn.close();
	}

	@After
	public void cleanUp() throws Exception {
		Connection _connection = DriverManager.getConnection(_jdbcConnection,
				username, password);
		String sql = "DROP DATABASE IF EXISTS " + database;
		Statement statement = _connection.createStatement();
		statement.executeUpdate(sql);
		statement.close();
	}

	public static IDatabaseConnection getConnection()
			throws ClassNotFoundException, DatabaseUnitException, SQLException {

		Class driverClass = Class.forName(_driverClass);
		Properties props = new Properties();
		props.put("user", username);
		props.put("password", password);

		// This is the most important step
		Connection jdbcConnection = DriverManager.getConnection(
				_jdbcConnection.replace("group2db", database), props);

		// This wraps the plain jdbc connection, created above
		// We can get that by doing connection.getConnection() which gives
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

		// The statements below are required to remove the following warning:
		//
		// [main] WARN org.dbunit.dataset.AbstractTableMetaData - Potential
		// problem found: The configured data type factory 'class
		// org.dbunit.dataset.datatype.DefaultDataTypeFactory' might cause
		// problems with the current database 'PostgreSQL' (e.g. some datatypes
		// may not be supported properly). In rare cases you might see this
		// message because the list of supported database products is incomplete
		// (list=[derby]). If so please request a java-class update via the
		// forums.If you are using your own IDataTypeFactory extending
		// DefaultDataTypeFactory, ensure that you override getValidDbProducts()
		// to specify the supported database products.

		DatabaseConfig databaseConfig = connection.getConfig();
		databaseConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
				new PostgresqlDataTypeFactory());
		return connection;
	}
}

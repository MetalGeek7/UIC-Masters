package test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Setup300MBTest {
	public static Connection conn;
	private static String _driverClass = "org.postgresql.Driver";

	// Do not change this
	private static String _jdbcConnection = "jdbc:postgresql://127.0.0.1:5432/group2db";
	private static String username = "postgres";
	private static String password = "password";

	@Before
	public void prepare() throws FileNotFoundException, SQLException,
			ClassNotFoundException {

		Class.forName(_driverClass);
		Properties props = new Properties();
		props.put("user", username);
		props.put("password", password);

		// This is the most important step
		conn = DriverManager.getConnection(_jdbcConnection, props);

		ScriptRunner runner = new ScriptRunner(conn);
		runner.setLogWriter(null);
		runner.setErrorLogWriter(null);
		runner.runScript(new FileReader("group2db.sql"));
		
		conn.setAutoCommit(true);

	}

	@Test
	public void populateDatabase() {
		boolean result = true;
		try {
			Scanner in = new Scanner(new FileInputStream("extra-data.sql"));
			Vector<String> statements = new Vector<String>(4773000);
			while (in.hasNextLine()) {
				statements.add(in.nextLine());
			}

			Statement statement = conn.createStatement();
			double over = 0.0;
			int totalStatements = statements.size();
			int i = 0;
			for (String sql : statements) {
				int rows = statement.executeUpdate(sql);
				i++;
				if (rows < 1) {
					result = false;
					break;
					
				}
				
				over = (double) i / totalStatements;
				
				System.out.println((int)(over*100) + " % Finished");

			}

		} catch (Exception e) {
			System.out.println(e);
			result = false;
		}
		assertTrue(result);
	}

	@After
	public void cleanup() {

	}

}

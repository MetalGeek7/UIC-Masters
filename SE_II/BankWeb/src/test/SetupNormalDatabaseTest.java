package test;

import static org.junit.Assert.*;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Properties;



import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SetupNormalDatabaseTest {
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
	public void dummy() {
		boolean result = true;
		assertTrue(result);
	}

	@After
	public void cleanup() {

	}

}

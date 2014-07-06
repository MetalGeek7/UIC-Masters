package cs442.group2.BankingApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;



public class BankConnect {
	
	private static Connection conn; // Connection object
	private static String username; // username for postgresql 
	private static String password; // password for postgresql
	private static String host;
	private static String port;
	private static String database;
	
	static{

		conn = null;
		username = "postgres";
		password = "password";
		port = "5432";
		host = "localhost";
		database = "group2db";
	}
	
	// To obtain a database connection used in the classes to connect to the PostgreSQL db. 
	public static synchronized Connection getConnection(){
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			Reporting.out.println("Postgresql could not be loaded");
			Reporting.out.println(e1);
		}
		if(conn == null){
			
			String connectionURL = "jdbc:postgresql://" + host + ":" + port + "/" + database;
//			connectionURL = "jdbc:postgresql://127.0.0.1:5432/group2db";
			Properties props = new Properties();
			props.put("user", username);
			props.put("password", password);
			
			try {
				conn = DriverManager.getConnection(connectionURL, props);			
				if(conn != null){
					conn.setAutoCommit(false);
					conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);	
				}				
			} catch (SQLException e) {
				Reporting.err.println(e);
				Reporting.showLog();
				System.exit(1);
			}
			
		}
		return conn;
	}
	
	// Used during dbunit testing
	public static void useConnection(Connection connection) throws SQLException {
		conn = connection;
		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		
	}
}

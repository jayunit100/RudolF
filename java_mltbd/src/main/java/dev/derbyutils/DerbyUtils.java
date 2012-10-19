package dev.derbyutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

import java.sql.PreparedStatement;

public class DerbyUtils {

	    private static final  String framework = "embedded";
	    private static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	    private static final String protocol = "jdbc:derby:";   
	    
	    /* Just a set of utility methods for the Derby database 
	     * Taken from the Apache demo class and some boiler plate mongo
	     * 
	     * */
	    
	    
	    
	    
	    // creates database   
	public static void makeDerby(String dbName) {
		loadDriver();
		Connection conn = null;
		Properties props = new Properties();// embedded no need, for server apps will contain auth data
		Statement s = null;
		try {conn = DriverManager.getConnection(protocol + dbName+ ";create=true", props);	
		conn.setAutoCommit(false);
		s = conn.createStatement();
		//this is just a  table of  test data
		s.execute("create table collectorData(num int, catalog_link varchar(140)," +
				"catalog_name varchar(80), catalog_id varchar(40), category_link varchar(140)," +
				"category_name  varchar(200), manufacturer varchar(40), myear varchar(40), dimensions varchar(40) )");
		
		 conn.commit();
		 conn.close();
		
		  } catch (SQLException e) {}
	}
	    
    
	    // inserts test data for a test loop    
	public static void goDerby(String dbName, HashMap<Integer, String> preparedStatements) {
		loadDriver();
		Connection conn = null;
	    PreparedStatement psInsert = null;
	    PreparedStatement psUpdate = null;
	    Statement s = null;
	   
		Properties props = new Properties();// embedded no need, for server apps will contain auth data
		
		try {conn = DriverManager.getConnection(protocol + dbName+ ";create=false", props);	
		conn.setAutoCommit(false);
		 
		s = conn.createStatement();

	//    psInsert = conn.prepareStatement("insert into collectorData values (?, ?)");
	    psInsert = conn.prepareStatement( preparedStatements.get(0)); 
	    Integer intObj = Integer.parseInt(preparedStatements.get(1));
	    int id = intObj.intValue(); 
		psInsert.setInt(1, id );
	    psInsert.setString(2, preparedStatements.get(2));
		psInsert.setString(3, preparedStatements.get(3));
	    psInsert.setString(4, preparedStatements.get(4));
		psInsert.setString(5, preparedStatements.get(5));
	    psInsert.setString(6, preparedStatements.get(6));
		psInsert.setString(7, preparedStatements.get(7));
	    psInsert.setString(8, preparedStatements.get(8));
	    psInsert.setString(9, preparedStatements.get(9));
        psInsert.executeUpdate();

        conn.commit();
        conn.close();
     
		
		   } catch (SQLException e) {}
	  
	}
	
	// check data and clean up, drop table as just test data
	public static void dropDerby(String dbName) {
		 System.out.println("dropDerby method");
		loadDriver();
		Connection conn = null;
		Properties props = new Properties();// embedded no need, for server apps will contain auth data
		Statement s = null;
		try {conn = DriverManager.getConnection(protocol + dbName+ ";create=false", props);	
		conn.setAutoCommit(false);
		
		ResultSet rs = null;
		s = conn.createStatement();
		 System.out.println("dropDerby.createStatement()");
		rs = s.executeQuery("SELECT * FROM collectorData");

		   while(rs.next()) {
		       
		        System.out.println("Id: "+rs.getInt(1));
		        System.out.println("catalog_link: "+rs.getString(2));
		        System.out.println("catalog_name: "+rs.getString(3));
		        System.out.println("catalog_id: "+rs.getString(4));
		        System.out.println("category_link: "+rs.getString(5));
		        System.out.println("category_name: "+rs.getString(6));
		        System.out.println("manufacturer: "+rs.getString(7));
		        System.out.println("year: "+rs.getString(8));
		        System.out.println("dimensions: "+rs.getString(9));      

		        }

		
		s.execute("drop table collectorData");
		conn.commit();
		DriverManager.getConnection("jdbc:derby:;shutdown=true");
		 } catch (SQLException e) {}
	}
	
	    
    
	    // utility methods below
	    
	    
	    private static void loadDriver() {

	        try {
	            Class.forName(driver).newInstance();
	            System.out.println("Setting up datbase insert");
	        } catch (ClassNotFoundException cnfe) {
	            System.err.println("\nUnable to load the JDBC driver " + driver);
	            System.err.println("Please check your CLASSPATH.");
	            cnfe.printStackTrace(System.err);
	        } catch (InstantiationException ie) {
	            System.err.println(
	                        "\nUnable to instantiate the JDBC driver " + driver);
	            ie.printStackTrace(System.err);
	        } catch (IllegalAccessException iae) {
	            System.err.println(
	                        "\nNot allowed to access the JDBC driver " + driver);
	            iae.printStackTrace(System.err);
	        }
	    }
}

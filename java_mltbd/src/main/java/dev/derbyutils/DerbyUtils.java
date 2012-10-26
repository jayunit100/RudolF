package dev.derbyutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

import java.sql.PreparedStatement;


/** 
 * Just a set of utility methods for the Derby database 
 * Taken from the Apache demo class and some boiler plate mongo
 * */
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
	s.execute("create table frankenData( id int, size int , object_name varchar(80) )");
	
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
	Integer intObj2 = Integer.parseInt(preparedStatements.get(2));
	int id2 = intObj2.intValue(); 		
    psInsert.setInt(2, id2);// size of object
	psInsert.setString(3, preparedStatements.get(3));
    psInsert.executeUpdate();

    conn.commit();
    conn.close();
 
	
	   } catch (SQLException e) {}
  
}

// check data and clean up, drop table as just test data
public static void dropDerby(String dbName) {
	System.out.println("report and dropDerby");
	loadDriver();
	Connection conn = null;
	Properties props = new Properties();// embedded no need, for server apps will contain auth data
	Statement s = null;
	try {conn = DriverManager.getConnection(protocol + dbName+ ";create=false", props);	
	conn.setAutoCommit(false);
	
	ResultSet rs = null;
	s = conn.createStatement();
	System.out.println("dropDerby method select all Statement()");
	rs = s.executeQuery("SELECT * FROM frankenData");

	   while(rs.next()) {
	       
	        System.out.println("Id: "+rs.getInt(1));
	        System.out.println("object size: "+rs.getInt(2));
	        System.out.println("object_name: "+rs.getString(3));
 

	        }

	System.out.println("dropDerby Statement()");
	s.execute("drop table frankenData");
	conn.commit();
	conn.close();
	DriverManager.getConnection("jdbc:derby:;shutdown=true");
	 } catch (SQLException e) {}
}

    

    // utility methods below
    
    
    private static void loadDriver() {

        try {
            Class.forName(driver).newInstance();
            System.out.println("load driver");
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

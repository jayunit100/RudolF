package dev.derbyutils;

import java.io.File;
import java.util.HashMap;

import com.mongodb.DBObject;

/* this is a worker thread that updates the embedded derby database
 * the objects of this type have a secondary function to create 
 * "out of memory" errors on the heap
 * 
 */

public class DerbyWorkerThread  implements Runnable {

	Thread t;
	final DBObject doc;
	private final HashMap < Integer, String> preparedStatements  = new HashMap < Integer, String>();
	private static String sep = File.separator;
	
	
	   public DerbyWorkerThread(final DBObject doc) {
		   this.doc  = doc;
		   
		     t = new Thread(this, "Derby Worker Thread");
		     t.start(); // Start the thread
		   
	}


	   
	   public void run() {
		
	    Integer  i  =  (Integer)doc.get("id");
		
		preparedStatements.put(0, "insert into collectorData values (?, ?, ?, ?, ?, ?, ?, ?, ? )");
		preparedStatements.put(1,i.toString());
		preparedStatements.put(2,(String)doc.get("catalog_link"));
		preparedStatements.put(3,(String)doc.get("catalog_name"));
		preparedStatements.put(4,(String)doc.get("catalog_id"));
		preparedStatements.put(5,(String)doc.get("category_link"));
		preparedStatements.put(6, (String)doc.get("category_name"));
		preparedStatements.put(7,(String)doc.get("manufacturer"));
		preparedStatements.put(8,(String)doc.get( "year"));
		if(doc.get( "dimensions")!=null) preparedStatements.put(9,(String) doc.get( "dimensions"));
		else preparedStatements.put(9, "no dimensions");
		
		// pause for a while as derby is slow
		
		try {
			Thread.sleep(1600);
			} catch (InterruptedException e) { };
		
			DerbyUtils.goDerby("e:dbDerb"+sep+"firstTest", preparedStatements);
           }// end run
		   
	   }// end class


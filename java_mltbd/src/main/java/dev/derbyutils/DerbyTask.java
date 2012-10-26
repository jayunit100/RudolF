package dev.derbyutils;

import java.io.File;
import java.util.HashMap;

import com.mongodb.DBObject;

import dev.unsafe.FrankenClass;


/* this is a worker thread that updates the embedded derby database
 * now has unsafe function
 * 
 */

public class DerbyTask   implements Runnable {

	Thread t;
	private static String sep = File.separator;
	private final HashMap < Integer, String> preparedStatements  = new HashMap < Integer, String>();
	private static Object testObj;
	
	
	   public DerbyTask (final Object o) {
		   this.testObj = o;
		   
	}
	   
	   public void run() {
		
		FrankenClass unsafeObject = new FrankenClass(testObj);
   
		preparedStatements.put(0, "insert into frankenData values (?, ?, ?  )");
		preparedStatements.put(1,"1");
		Long longObj = null;
		try {
			longObj = unsafeObject.sizeOf();} catch (SecurityException e1) {e1.printStackTrace();} catch (NoSuchFieldException e1) {
			e1.printStackTrace();} catch (IllegalArgumentException e1) {e1.printStackTrace();} catch (IllegalAccessException e1) {
			e1.printStackTrace(); } /* */
		
		 System.out.println(longObj.toString());
		preparedStatements.put(2,longObj.toString());// this will be size of object
	//	preparedStatements.put(2,"4");// this will be size of object
		preparedStatements.put(3,testObj.toString());

		
		// pause for a while as derby is slow
		
		try {
			Thread.sleep(400);
			} catch (InterruptedException e) { };
		
			DerbyUtils.goDerby("benchMarkResults", preparedStatements);
           }// end run
		   
	   }// end class


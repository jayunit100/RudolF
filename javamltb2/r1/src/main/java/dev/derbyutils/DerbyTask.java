package dev.derbyutils;

import java.io.File;
import java.util.HashMap;

import dev.unsafe.FrankenClass;


/**
 * This is a worker thread that updates the embedded derby database
 * the objects of this type have a secondary function to create 
 * "out of memory" errors on the heap
 */

public class DerbyTask   implements Runnable {
	private static String sep = File.separator;
	private final HashMap < Integer, String> preparedStatements  = new HashMap < Integer, String>();
	private static Object testObj;
	private static Integer id;
	
	   public DerbyTask (final Object o, int k) {
		   this.testObj = o;
		   this.id = k;
	}
	   
	   public void run() {
		
		FrankenClass unsafeObject = new FrankenClass(testObj);
   
		preparedStatements.put(0, "insert into frankenData values (?, ?, ?  )");
		preparedStatements.put(1,id.toString());
		Long longObj = null;
		try {
			longObj = unsafeObject.sizeOf();} catch (SecurityException e1) {e1.printStackTrace();} catch (NoSuchFieldException e1) {
			e1.printStackTrace();} catch (IllegalArgumentException e1) {e1.printStackTrace();} catch (IllegalAccessException e1) {
			e1.printStackTrace(); } /* */
		
		preparedStatements.put(2,longObj.toString());// this will be size of object
		preparedStatements.put(3,testObj.toString());

		
		// pause for a while as derby is slow
		
		try {
			Thread.sleep(400);
			} catch (InterruptedException e) { };
		
			DerbyUtils.goDerby("benchMarkResults", preparedStatements);
           }// end run
		   
	   }// end class


package dev.derbyutils;

import java.io.File;
import java.util.HashMap;

/**
 * This is a worker thread that updates the embedded derby database
 * the objects of this type have a secondary function to create 
 * "out of memory" errors on the heap
 */
public class DerbyWorkerThread implements Runnable {

	Thread t;
	private static String sep = File.separator;
	private final HashMap < Integer, String> preparedStatements  = new HashMap < Integer, String>();
	private static Object testObj;
	

	public DerbyWorkerThread(final Object o) {
		this.testObj = o;

	}

	public void run() {
		preparedStatements.put(0, "insert into frankenData values (?, ?, ?  )");
		preparedStatements.put(1,"1");
		Long longObj = null;
	
	//	preparedStatements.put(2,longObj.toString());// this will be size of object
	//	preparedStatements.put(2,"4");// this will be size of object
		preparedStatements.put(3,testObj.toString());


		try {
			Thread.sleep(1600);
		} catch (InterruptedException e) {
		}
		DerbyUtils.goDerby("benchMarkResults", preparedStatements);
	}// end run
}// end class


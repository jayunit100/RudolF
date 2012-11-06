package dev.derbyutils;

import java.io.File;
import java.util.HashMap;

import dev.unsafe.FrankenClass;

/*  This class updates the embedded Derby database.
 * 	 
 * 
 *  @ testObj an object where its memory footprint in bytes is measured by sun.misc.Unsafe.class
 * 
 *  @ Franken class is a wrapper class to obtain an instance of sun.misc.Unsafe.class 
 *  
 *  demonstrate important technique of obtaining the size of an object on bytes important for
 *  direct memory copy analogous to array.length in array copy
 *  
 *  
 * 
 */

public class DerbyTask implements Runnable {
	private static String sep = File.separator;
	private final HashMap<Integer, String> preparedStatements = new HashMap<Integer, String>();
	private static Object testObj;
	private static Integer id;

	public DerbyTask(final Object o, int k) {
		this.testObj = o;
		this.id = k;
	}

	public void run() {

		FrankenClass unsafeObject = new FrankenClass(testObj);

		preparedStatements.put(0, "insert into frankenData values (?, ?, ?  )");
		preparedStatements.put(1, id.toString());
		Long longObj = null;
		try {
			longObj = unsafeObject.sizeOf();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} /* */

		preparedStatements.put(2, longObj.toString());// this will be size of
														// object
		preparedStatements.put(3, testObj.toString());

		// pause for a while as derby is slow

		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
		}
		;

		DerbyUtils.goDerby("benchMarkResults", preparedStatements);
	}

}


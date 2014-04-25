package dev.benchmarks;

import java.lang.management.ManagementFactory;

public class MemoryTest extends BenchMarkBase {
	
	/* Very simple class to compare the different ways to make
	 *  memory measurement calls

	 *  The Runtime.freeSpace method might not be accurate down to the byte level.
	 *  Caching performed by the class when the first instance is created can skew the results.
	 *  Garbage collection can be unpredictable.
	 *  
	 *  In this class we will look into this and see what we can find out
	 * 
	 */
	
	
     
	// later this method can be expanded to deal with arbitary types passed in by class name 
	public void genericCollectionAnalyzer() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		// initila values for pre and post object creation
		long usedMemoryXBean = 0;
		long usedMemoryRunTime = 0;
		long initialMemoryXBean = 0;
		long initialMemoryRunTime = 0;

		Object[] objects = new Object[100];
		long size = 0;

		// we do this because later this method can be extended to analyze class
		// types passed in as a parameter
		Class clazz = Class.forName("java.util.Hashtable");

		// get the initial memory using runtime and xbeans
		initialMemoryXBean = ManagementFactory.getMemoryMXBean()
				.getHeapMemoryUsage().getUsed();
		initialMemoryRunTime = Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();

		// Create a primer instance of the class before your measure the heap
		// the first time
		Object primer = clazz.newInstance();
		for (int i = 0; i < objects.length; i++) {
			objects[i] = clazz.newInstance();
		}
		// get the final memory using runtime and xbeans
		usedMemoryXBean = ManagementFactory.getMemoryMXBean()
				.getHeapMemoryUsage().getUsed();
		usedMemoryRunTime = Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();

		// simple routine to calculate an average size for 100 objects
		float approxSizeXBean = (usedMemoryXBean - initialMemoryXBean) / 100f;
		approxSizeXBean = Math.round(approxSizeXBean);
		// simple routine to calculate an average size for 100 objects
		float approxSizeRunTime = (usedMemoryRunTime - initialMemoryRunTime) / 100f;
		approxSizeRunTime = Math.round(approxSizeRunTime);

		System.out.println("size of empty hashtable by XBean = "
				+ approxSizeXBean + " size of empty hashtable by RunTime = "
				+ approxSizeRunTime);

	}
	
	
	
	// later will use for comparison for number of gc calls
	private void lightGc() {  
	    try {  
	        System.gc();  
	        Thread.sleep(200);  
	        System.runFinalization();    
	        System.gc();  
	    } catch (InterruptedException ex) {  
	        ex.printStackTrace();  
	    }  
	} 
	
	
	
	// later will use for comparison for number of gc calls
	private void heavyGc() {  
	    try {  
	        System.gc();  
	        Thread.sleep(200);  
	        System.runFinalization();  
	        Thread.sleep(200);  
	        System.gc();  
	        Thread.sleep(200);  
	        System.runFinalization();  
	        Thread.sleep(1000);  
	        System.gc();  
	        Thread.sleep(200);  
	        System.runFinalization();  
	        Thread.sleep(200);  
	        System.gc();  
	    } catch (InterruptedException ex) {  
	        ex.printStackTrace();  
	    }  
	} 

}

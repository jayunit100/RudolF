package dev.benchmarks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**   This class obtains the  Benchmark results for the Synchronization
 *	  Keeping a lock for longer than required, can slow down the other threads.
 *    Multi-threading optimization is one of those situations where there are 
 *    "best practices" that generally hold true, but you are not going to get rule that 
 *    works in all situations. So we will test and see
 *
 *		@volatileInt volatile field for comparison with non volatile fields
 * 		@lock simple reentrant lock
 *		@syncMethod() Synchronized Method
 *  	@syncCodeBlock() Synchronized Block
 *		@syncCodeBlockWithVolatile() Synchronized Block with Volatile field
 *		@codeBlockWithLock() block with simple reentrant lock
 * 	  from this class we can learn the performace cost of the JVM's Synchronization scheme
 *
 */


public class SynchronizerBenchMark extends BenchMarkBase {
	
	
	private  volatile int volatileInt = 0;
	private final Lock lock = new ReentrantLock();
	
	
	//Synchronized Method
	public synchronized void  syncMethod() {
		long a = System.currentTimeMillis(); 		
	     for (int i=0; i<1000000; i++)   {
	    	int n=5*5;
	     }
            System.out.println("Synchronized Method "+(System.currentTimeMillis()-a)+"s");
	
	}
	
	// Synchronized Block
	public void syncCodeBlock() {
		long a = System.currentTimeMillis();
		//synchronized statement
	     synchronized(this) {	    	 
	    	 for (int i=0; i<1000000; i++)   {
		    	int  n=5*5;
		     } 
	     }// end lock
	     
	     System.out.println("Sync Block "+(System.currentTimeMillis()-a)+"s"); 
	     
	
	}
	
	//Synchronized Block with Volatile field
	public void syncCodeBlockWithVolatile() {
		long a = System.currentTimeMillis();
		//synchronized statement
	     synchronized(this) {	    	 
	    	 for (int i=0; i<1000000; i++)   {
	    		 volatileInt =5*5;
		     } 
	     }// end lock
	     
	     System.out.println("Volatile Sync Block "+(System.currentTimeMillis()-a)+"s"); 
	     
	
	}
	
	

	
	

}

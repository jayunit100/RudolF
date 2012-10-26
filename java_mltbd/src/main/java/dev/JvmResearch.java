package dev;
import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dev.derbyutils.DerbyTask;
import dev.derbyutils.DerbyUtils;


/**
 * Main class JVM research
 * 
 */
public class JvmResearch   {
	
	/* 
	 * idea is to set up a resource intensive process to test JVM
	 * 
	 * \
	 * Apache derby is a pure java embedded object that will be main targert of profiling, as hopefully it will 
	 * consume lots of memory on the heap
	 * 
	 * 
	 * this loop will also be a bottle neck to be investigated for the stack memory and loop optimisation
	 * 
	 * By making the boiler plate loop spawn a thread for each JDBC  derby update I can generate an out 
	 * of memory error in a controlled fashion by setting the number of loops, and/or changing the data through put for each 
	 * iteration
	 * 
	 * 
	 * 
	 */
	
	

	private static String sep = File.separator;
	private static int k ;;
	
	public static void main(String[] args) throws Exception{
		

		

		
		 /* this will be a resource intensive loop for investigation by profiling tools
		  * now has new concurrency and unsafe implementation
		  *  
		  *  
		  */
		
		DerbyUtils.makeDerby("benchMarkResults");
		
		ExecutorService threadExecutor = Executors.newCachedThreadPool();
		
		k=1;
		  try {
	       //     while(true) { 
	            	       	
	            	DerbyTask derbyTask = new DerbyTask(new Date());
	            	// pause for a while as derby is slow
	            	threadExecutor.execute(derbyTask ); 
	            	
	            	
	            	try {
	        			Thread.sleep(400);
	        			} catch (InterruptedException e) { };
	        			System.out.println("insert: "+(k++)+", completed");
	        			
	       //     }// end while
		

	        } finally {
	         
	         // all done drop the database as is just junk test data
	        	System.out.println("report Derby");
	            System.out.println("dropDerby");
	            threadExecutor.shutdown();
	           
	            DerbyUtils.dropDerby("benchMarkResults");     
	        }
		
		  
	}
}

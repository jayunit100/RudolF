package dev;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import dev.derbyutils.DerbyTask;
import dev.derbyutils.DerbyUtils;
import dev.testobjects.StructureWrapper;

/* 
 * ideas
 * 1 set up a resource intensive process to test JVM
 * 2 investigate Direct Memory Access (DMA) in Java
 * derby is a pure java embedded object that can be  target of profiling and obvious store bench mark results
 * 
 * main loop  to be investigated for the stack memory and loop optimization
 * 
 * Franken class is sun.misc.Unsafe.class wrapper for objects to be investigated and manipulated with DMA
 * 
 * struct list is just a wrapper for some structures for testing Unsafe
 */
public class JvmResearch   {
	

	// this code may be helpful for java learners as shows a few things, here is for initial testing for Unsafe behaviour
	private static final List<Object> structList;
	 
	static {  structList = Arrays.asList( new StructureWrapper(). new MyStructureEmpty(), 
			new StructureWrapper().new MyStructureOneInt(),  new StructureWrapper().new MyStructureTwoInt() );
	}

	private static String sep = File.separator;
	private static Integer k ;
	
	public static void main(String[] args) throws Exception{		
		

		/* results database */
		
		DerbyUtils.makeDerby("benchMarkResults");
		
		ExecutorService threadExecutor = Executors.newCachedThreadPool();
		
		k=1;// count the loops if needed
		  try {
	            for (Object obj : structList)  { 
	            	// pass thread an object to be investigated or manipulated       	
	            	DerbyTask derbyTask = new DerbyTask(obj, k);
	            	// pause for a while as derby is slow
	            	threadExecutor.execute(derbyTask ); 
	            	
	            	
	            	try {
	        			Thread.sleep(400);
	        			} catch (InterruptedException e) { };
	        			System.out.println("insert: "+(k++)+", completed");
	        			
	           }// end loop
		

	        } finally {
	         
	         // all done drop the database as is just junk test data for now
	        	System.out.println("report Derby");
	            System.out.println("dropDerby");
	            threadExecutor.shutdown();
	           
	            DerbyUtils.dropDerby("benchMarkResults");     
	        }
		
		  
	}
}

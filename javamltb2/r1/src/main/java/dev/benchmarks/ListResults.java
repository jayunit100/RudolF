package dev.benchmarks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dev.dump.Dumper;


/*   This class obtains the  Benchmark Results for the CopyOnWriteArrayList 
 *   compared to the standard java.util.* Collections. This class spawns 
 *   a thread data aggregation process, this data is returned to the main 
 *   BenchMarkRunner class. At the moment the thread data aggregation only occurs
 *   in the  copyOnWriteArrayListResult() method for simplicity but it can be called at any 
 *   point in the code
 * 
 *  @ resultsMap  container for list results stats
 *  
 *  @ copyOnWriteArrayListResult() runs loop for CopyOnWriteArrayList
 *  
 *  @ synchronizedListResult() runs loop for synchronizedList
 *  
 *  @ displayMemory() method measures memory of map during benchmark
 * 
 */



public class ListResults implements Callable<HashMap<String, HashMap<String, String>>> {
	
	final HashMap<String, HashMap<String, String>> resultsMap = new HashMap<String, HashMap<String, String>>();
	
	// ExecutorService for lifecycle methods for threads
	// in this class the threads are spawned for the thread dump aggregating/report
	private final  ExecutorService threadExecutor = Executors.newCachedThreadPool();	
	// container for bench mark results will be returned to the BenchMarkRunner Class
	private final HashMap<String, String> dumpResults = new  HashMap<String, String>();
	
	// java.util.concurrent.Future for Dumper instance to obtain thread data
	private  Future<String> listDumpTask;
	// Dumper instance to obtain thread data while Map benchmark process runs
	public final Dumper dumpTask;
	
	
	//constructor
	public ListResults(final Dumper dumpTask) {
		super();
		this.dumpTask = dumpTask;
	}

	// java.util.concurrent.Callable. Callables unlike Runnables allow for return values.
	// here we return a String which is a report of the bench marks and all threads running during those bench marks
	@Override
	public HashMap<String, HashMap<String, String>> call() {
		
		// get and store the bench mark results for the "copyOnWriteArray" and "synchronizedList"
		resultsMap.put("copyOnWriteArray", copyOnWriteArrayListResult() );		
		resultsMap.put("synchronizedList", copyOnWriteArrayListResult() );
		
		// get the thread data while the copyOnWriteArrayList benchmark is running
		try {
			dumpResults.put("single" ,listDumpTask.get());
			
		} catch (InterruptedException e) {e.printStackTrace();
		} catch (ExecutionException e) {e.printStackTrace();
		}
		
		// store the thread data
		resultsMap.put("dumpReport", dumpResults);
	
	//all tasks finished shut down the threads	
	threadExecutor.shutdown();
	
	// return all results to the NenchMarkRunner class
	return resultsMap;
	}
	
	// this method runs the bench mark for the java.util.concurrent.CopyOnWriteArrayList
	public HashMap<String,String> copyOnWriteArrayListResult() {
	// store the results of the benchmark run in this map to return	
	final HashMap<String,String> results = new HashMap<String, String>();
	// List for bench mark
	final CopyOnWriteArrayList<Object> syncList = new CopyOnWriteArrayList<Object>();
	// utility variables for test
	long mem = 0;
  	int k =1;
  	
  	// run the thread dump class and aggregate thread data during the loop
  	listDumpTask = threadExecutor.submit(dumpTask);
  	
  	for(int i = 0; i< 100; i++) {
  		 

         k++;
         syncList.add (new Object());
         mem =  mem +  displayMemory();
  		 
  	 }
  	 // get a normalised memory result for comparison
		mem =mem/k;	
	 	results.put("memStat",""+mem/k);	
	 	results.put("instStat",""+ k);
	 	
	 // all done return the benchmark results	 
	  return results;	

			}
	
	// this method runs the bench mark for the java.util.Collections.synchronizedList
	public HashMap<String, String> synchronizedListResult() {
	// store the results of the benchmark run in this map to return	
	final HashMap<String, String> results = new HashMap<String, String>();
	// List for bench mark
	List<Object> syncList = Collections.synchronizedList(new ArrayList<Object>());
	
	 // utility variables for test
	long mem = 0;
  	int k =1;
	
  	 for(int i = 0; i< 100; i++) {
  		 

         k++;
         syncList.add (new Object());
         mem =  mem +  displayMemory();
  		 
  	 }
 	// get a normalised memory result for comparison
		mem =mem/k;
	 	results.put("memStat",""+mem/k);	
	 	results.put("instStat",""+ k);
	 	 
	// all done return the benchmark results
	  return results;	

			}

	// get the memory footprint of the map at a point in the loop
	public static long displayMemory() {
	    Runtime r=Runtime.getRuntime();
	    r.gc();
	    r.gc(); // gc sweep one is not enough
	   return r.totalMemory()-r.freeMemory();
	}

}

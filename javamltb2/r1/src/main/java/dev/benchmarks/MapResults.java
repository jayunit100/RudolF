package dev.benchmarks;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dev.dump.Dumper;

/*   This class obtains the  Benchmark Results for the ConcurrentMap 
 *   compared to the standard java.util.* Collections. This class spawns 
 *   a thread data aggregation process, this data is returned to the main 
 *   BenchMarkRunner class. At the moment the thread data aggregation only occurs
 *   in the  hashMapResult() method for simplicity but it can be called at any 
 *   point in the code
 * 
 *  @ resultsMap  container for list results stats
 * 
 *  @ hashMapResult() runs loop for HashMap
 *  
 *  @ concurrentHashMapResult() runs loop for ConcurrentHashMap
 * 
 *  @ hashtableResult() runs loop for HashTable
 *  
 *  @ displayMemory() obtains memory footprint of collection in loop
 *  
 *  @ hashMapResult() method runs the benchmark for the java.util.HashMap
 *  
 *  @ concurrentHashMapResult() method runs the benchmark for the java.util.concurrent.ConcurrentHashMap
 *  
 *  @ hashTableResult() method runs the benchmark for the java.util.Hashtable;
 *  
 *  @ displayMemory() method measures memory of map during benchmark
 * 
 */



public class MapResults implements Callable<HashMap<String, HashMap<String, String>>>  {
	
	// ExecutorService for lifecycle methods for threads
	// in this class the threads are spawned for the thread dump aggregating/report
	private final  ExecutorService threadExecutor = Executors.newCachedThreadPool();
	
	// container for bench mark results will be returned to the BenchMarkRunner Class
	private final HashMap<String, HashMap<String, String>> resultsMap = new HashMap<String, HashMap<String, String>>();
	
	// container for thread dump results will be returned to the BenchMarkRunner Class
	private final HashMap<String, String> dumpResults = new  HashMap<String, String>();
	
	// Dumper instance to obtain thread data while Map benchmark process runs
	private final Dumper dumpTask;
	
	// java.util.concurrent.Future for Dumper instance to obtain thread data
	private  Future<String> mapDumpTask;


	// constructor
	public MapResults(final Dumper dumpTask) {
		super();
		this.dumpTask = dumpTask;
	}

	
	
	// java.util.concurrent.Callable. Callables unlike Runnables allow for return values.
	// here we return a String which is a report of the bench marks and all threads running during those bench marks
	@Override
	public HashMap<String, HashMap<String, String>> call() {
		
		// get and store the bench mark results 
		
		resultsMap.put("hashMap", hashMapResult());
		resultsMap.put("concurrentMap", concurrentHashMapResult() );
		resultsMap.put("hashTable", hashTableResult());
		
		
		// get the thread data while the hashMapResult benchmark is running
		try {
			dumpResults.put("single" ,mapDumpTask.get());
			
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

	
	
	// this method runs the bench mark for the java.util.HashMap
	public HashMap<String, String> hashMapResult() {
	
	// store the results of the benchmark run in this map to return
	final HashMap<String, String> results = new HashMap<String,String>();
	// map for bench mark
  	final Map<Object,Object> testMap = new HashMap<Object, Object>();
  	// utility variables for test
  	long mem = 0;
  	int k =1;
  	
  	// run the thread dump class and aggregate thread data during the loop
  	mapDumpTask = threadExecutor.submit(dumpTask);
  	
  	  for(int i = 0; i< 100; i++) {

	            k++;
	            testMap.put(new Object(),new Object());
	            mem =  mem +  displayMemory();

	            }
  	  // get a normalised memory result for comparison
  	  			mem =mem/k;
			 	results.put("memStat",""+mem/k);	
	// return instance count, this must be extended, initially I tried to 
	//have a timed bench mark but ran into problems implementing the Scheduling code, 
	//I will try and extend to this later, with this extension this variable will measure speed	 	
			 	results.put("instStat",""+ k);
	
	// all done return the benchmark results	 
	  return results;	

			}
	
	// this method runs the bench mark for the java.util.HashMap
	public HashMap<String, String> concurrentHashMapResult() {
	// store the results of the benchmark run in this map to return	
	final HashMap<String, String> results = new HashMap<String, String>();
	// map for bench mark
	final Map<Object,Object> testSync = new ConcurrentHashMap<Object,Object>();
	
	// utility variables for test
  	long mem = 0;
  	int k =1;
  	
  	  for(int i = 0; i< 100; i++) {

	            k++;
	            testSync.put(new Object(),new Object());
	            mem =  mem +  displayMemory();

	            }
  	 // get a normalised memory result for comparison
		mem =mem/k;
	 	results.put("memStat",""+mem/k);	
	 	results.put("instStat",""+ k);

	 // all done return the benchmark results	 
	  return results;	

			}
	 
	
		public HashMap<String,String> hashTableResult() {
			// store the results of the benchmark run in this map to return
			final HashMap<String, String> results = new HashMap<String, String>();
			// map for bench mark
			final Hashtable<Object,Object> table = new Hashtable<Object, Object>();
			
			 // utility variables for test
			long mem = 0;
		  	int k =1;
		  	
		  	  for(int i = 0; i< 100; i++) {

			            k++;
			            table.put(new Object(),new Object());
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

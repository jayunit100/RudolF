package dev.benchmarks;

import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dev.dump.Dumper;



/*  This class is the entry point for the benchmarking, it obtains a report String
 *  from the bench mark classes MapResults and ListResults, it then passes this along to the main class
 *  for reporting as a screen dump String. During the benchmarking we poll the thread groups for thread 
 *  data and aggregate this into the bench mark report.
 *  
 *  From this class we can measure the memory cost of the java.util.concurrent  Collections
 *  CopyOnWriteArrayList and ConcurrentMap compared to the standard Collections classes
 * 
 *  From this class we can gain understanding of concurrency and its implications for the Java Memory Model
 *  
 *  @ mapTask is  benchmark thread for concurrent map
 * 
 *  @ listTask benchmark thread for concurrent list
 *  
 *  @ mapBenchMarks benchmark statistics container for map results
 * 
 *  @ listBenchMarks benchmark statistics container for list results 
 *  
 *  @ listBenchMarks benchmark statistics container for list results
 *  
 *  The following maps are just containers for benchmark results
 *  
 *  @ hashMapResult benchmark result for "java.util.HashMap"
 *   
 *  @ concurrentMapResult benchmark result for "java.util.concurrent.ConcurrentHashMap"
 *   
 *  @ hashtableResult benchmark result for  "java.util.Hashtable"
 *   
 *  @ copyOnWriteArrayResult benchmark result for "java.util.concurrent.CopyOnWriteArrayList"
 *   
 *  @ synchronizedListResult  benchmark result for  "java.util.Collections.synchronizedList"
 * 
 */

public class BenchMarkRunner implements Callable<String> {

	
	// ExecutorService for lifecycle methods for threads
	// these are the two map and list benchmark threads
	private  ExecutorService threadExecutor = Executors.newCachedThreadPool();
	
	// java.util.concurrent.Future for running benchmark for  map classes
	private  Future<HashMap<String, HashMap<String, String>>> mapTask;
	// java.util.concurrent.Future for running benchmark for  list classes
	private  Future<HashMap<String, HashMap<String, String>>> listTask;
	
	//benchmark results for  map classes
	private  HashMap<String, HashMap<String, String>> mapBenchMarks;	
	//benchmark results for  list classes
	private  HashMap<String, HashMap<String, String>> listBenchMarks;
	
	// This aggregates the results for reporting to the screen
	private StringWriter output = new StringWriter();
	
	
	// java.util.concurrent.Callable. Callables unlike Runnables allow for return values.
	// here we return a String which is a report of the bench marks and all threads running during those bench marks
		@Override
		public String call() {
			
	//  java concurrency Future tasks to run collection benchmarks	

	// use the  ExecutorService to submit a Callable and get a future for the Map Benchmark
		mapTask = threadExecutor.submit(new MapResults( new Dumper("Map BenchMarks")));
			
	// use the  ExecutorService to submit a Callable and get a future for the List Benchmark		
		listTask = threadExecutor.submit(new ListResults(new Dumper("List BenchMarks")));

		try {
			
		//retrieve the results from the callable's			
		mapBenchMarks = mapTask.get();			
		listBenchMarks = listTask.get();
	
		// now we obtain the results from the benchmarking and store them
		HashMap<String, String> hashMapResult = mapBenchMarks.get("hashMap");
		HashMap<String, String> concurrentMapResult = mapBenchMarks.get("concurrentMap");
		HashMap<String, String> hashtableResult = mapBenchMarks.get("hashTable");
		HashMap<String, String> copyOnWriteArrayResult = listBenchMarks.get("copyOnWriteArray");		
		HashMap<String, String> synchronizedListResult = listBenchMarks.get("synchronizedList");
		
		
		// construct the report from the benchmark and thread data
		
		// data for maps
		output.append("Java Collection Map Benchmark memory stats: "+"\n");
		output.append(""+"\n");
		output.append("hashMap memory: " + hashMapResult.get("memStat"+"\n"));
		output.append("concurrentMap memory: "+ concurrentMapResult.get("memStat")+"\n");
		output.append("hashtable memory: " + hashtableResult.get("memStat")+"\n");
		output.append(""+"\n");
		
		// get the thread dump data for the Map Benchmark and append it to the report string
		output.append("Map Benchmark Thread dump task result:"+"\n"+mapBenchMarks.get("dumpReport"));
		output.append(""+"\n");
		
		// data for lists
		output.append("Java Collectiond List Benchmark memory stats: "+"\n");
		output.append("");
		output.append("copyOnWriteArray memory: "+ copyOnWriteArrayResult.get("memStat")+"\n");
		output.append("synchronizedList memory: "+ synchronizedListResult.get("memStat")+"\n"+"\n");
		
		// get the thread dump data for the List Benchmark and append it to the report string
		output.append("Map Benchmark Thread dump task result:"+"\n"+listBenchMarks.get("dumpReport"));
		
		// bench marks completed shut down the bench mark threads
		threadExecutor.shutdown();
		
		} catch (InterruptedException e) { 
		// get localised message is more helpfull
			output.append("problems during bench mark run:"+"\n"+e.getLocalizedMessage());
		} catch (ExecutionException e) {
			output.append("problems during bench mark run:"+"\n"+e.getLocalizedMessage());
		}
		// return the report to the main class
		return output.toString();
		
		

	}

}

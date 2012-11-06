package dev.benchmarks;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



/*  This class is the entry point for the benchmarking and optimization code
 * 	results will be stored in an embedded Derby database.
 * 
 *  @ mapTask  benchmark for concurrent map
 * 
 *  @ listTask benchmark for concurrent list
 *  
 *  @ mapBenchMarks benchmark stats container for map results
 * 
 *  @ listBenchMarks benchmark stats container for list results 
 *    
 * 
 */



public class BenchMarkRunner {

	static ExecutorService threadExecutor = Executors.newCachedThreadPool();
	static Future<HashMap<String, HashMap<String, Long>>> mapTask;
	static Future<HashMap<String, HashMap<String, Long>>> listTask;
	static HashMap<String, HashMap<String, Long>> mapBenchMarks;
	static HashMap<String, HashMap<String, Long>> listBenchMarks;

	public static void getResults() {

		mapTask = threadExecutor.submit(new MapResults());
		listTask = threadExecutor.submit(new ListResults());

		try {
			mapBenchMarks = mapTask.get();
			listBenchMarks = listTask.get();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		HashMap<String, Long> hashMapResult = mapBenchMarks.get("hashMap");
		HashMap<String, Long> concurrentMapResult = mapBenchMarks.get("concurrentMap");
		HashMap<String, Long> hashtableResult = mapBenchMarks.get("hashTable");
		HashMap<String, Long> copyOnWriteArrayResult = listBenchMarks.get("copyOnWriteArray");		
		HashMap<String, Long> synchronizedListResult = listBenchMarks.get("synchronizedList");
		
		System.out.println("Java Collectiond Map Benchmark memory stats: ");
		System.out.println("");
		System.out.println("hashMap memory: " + hashMapResult.get("memStat"));
		System.out.println("concurrentMap memory: "+ concurrentMapResult.get("memStat"));
		System.out.println( "hashtable memory: " + hashtableResult.get("memStat"));
		System.out.println("");
		System.out.println("Java Collectiond List Benchmark memory stats: ");
		System.out.println("");
		System.out.println("copyOnWriteArray memory: "+ copyOnWriteArrayResult.get("memStat"));
		System.out.println("synchronizedList memory: "+ synchronizedListResult.get("memStat"));

		threadExecutor.shutdown();

	}

}

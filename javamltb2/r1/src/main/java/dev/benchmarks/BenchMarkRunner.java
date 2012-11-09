package dev.benchmarks;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dev.dump.Dumper;



/*  This class is the entry point for the benchmarking 
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

public class BenchMarkRunner implements Callable<String> {

	private  ExecutorService threadExecutor = Executors.newCachedThreadPool();
	private  Future<HashMap<String, HashMap<String, String>>> mapTask;
	private  Future<HashMap<String, HashMap<String, String>>> listTask;
	
	private  HashMap<String, HashMap<String, String>> mapBenchMarks;
	private  HashMap<String, HashMap<String, String>> listBenchMarks;
	
	private StringWriter output = new StringWriter();
	
	
	
	

	@Override
		public String call() {

		mapTask = threadExecutor.submit(new MapResults( new Dumper("Map BenchMarks")));
		
		//  java concurrency Future tasks to run collection benchmarks
				
		listTask = threadExecutor.submit(new ListResults(new Dumper("List BenchMarks")));

		try {
						
			mapBenchMarks = mapTask.get();
			
			listBenchMarks = listTask.get();
	
		
		HashMap<String, String> hashMapResult = mapBenchMarks.get("hashMap");
		HashMap<String, String> concurrentMapResult = mapBenchMarks.get("concurrentMap");
		HashMap<String, String> hashtableResult = mapBenchMarks.get("hashTable");
		HashMap<String, String> copyOnWriteArrayResult = listBenchMarks.get("copyOnWriteArray");		
		HashMap<String, String> synchronizedListResult = listBenchMarks.get("synchronizedList");
		
		output.append("Java Collection Map Benchmark memory stats: "+"\n");
		output.append(""+"\n");
		output.append("hashMap memory: " + hashMapResult.get("memStat"+"\n"));
		output.append("concurrentMap memory: "+ concurrentMapResult.get("memStat")+"\n");
		output.append("hashtable memory: " + hashtableResult.get("memStat")+"\n");
		output.append(""+"\n");
		output.append("Map Benchmark Thread dump task result:"+"\n"+mapBenchMarks.get("dumpReport"));
		output.append(""+"\n");
		
		
		output.append("Java Collectiond List Benchmark memory stats: "+"\n");
		output.append("");
		output.append("copyOnWriteArray memory: "+ copyOnWriteArrayResult.get("memStat")+"\n");
		output.append("synchronizedList memory: "+ synchronizedListResult.get("memStat")+"\n"+"\n");
		output.append("Map Benchmark Thread dump task result:"+"\n"+listBenchMarks.get("dumpReport"));
		threadExecutor.shutdown();
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		return output.toString();
		
		

	}

}

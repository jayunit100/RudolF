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


/*  This class obtains the List Benchmark Results
 * 
 *  @ resultsMap  container for list results stats
 * 
 *  @ copyOnWriteArrayListResult() runs loop for CopyOnWriteArrayList
 *  
 *  @ synchronizedListResult() runs loop for synchronizedList
 * 
 *  @ displayMemory() obtains memory footpring of collection in loop
 * 
 */



public class ListResults implements Callable<HashMap<String, HashMap<String, String>>> {
	
	final HashMap<String, HashMap<String, String>> resultsMap = new HashMap<String, HashMap<String, String>>();
	private final  ExecutorService threadExecutor = Executors.newCachedThreadPool();
	private final HashMap<String, String> dumpResults = new  HashMap<String, String>();
	private  Future<String> listDumpTask;
	
	public final Dumper dumpTask;
	
	public ListResults(final Dumper dumpTask) {
		super();
		this.dumpTask = dumpTask;
	}

	@Override
	public HashMap<String, HashMap<String, String>> call() {

		resultsMap.put("copyOnWriteArray", copyOnWriteArrayListResult() );
		
		resultsMap.put("synchronizedList", copyOnWriteArrayListResult() );
		
		try {
			dumpResults.put("single" ,listDumpTask.get());
			
		} catch (InterruptedException e) {e.printStackTrace();
		} catch (ExecutionException e) {e.printStackTrace();
		}
		resultsMap.put("dumpReport", dumpResults);
	
	threadExecutor.shutdown();	
	return resultsMap;
	}
	
	
	public HashMap<String,String> copyOnWriteArrayListResult() {
		
	final HashMap<String,String> results = new HashMap<String, String>();
	final CopyOnWriteArrayList<Object> syncList = new CopyOnWriteArrayList<Object>();
	long mem = 0;
  	int k =1;
  	listDumpTask = threadExecutor.submit(dumpTask);
  	 for(int i = 0; i< 100; i++) {
  		 

         k++;
         syncList.add (new Object());
         mem =  mem +  displayMemory();
  		 
  	 }
		mem =mem/k;
	 	results.put("memStat",""+mem/k);	
	 	results.put("instStat",""+ k);
	
	  return results;	

			}
	
	
	public HashMap<String, String> synchronizedListResult() {
	final HashMap<String, String> results = new HashMap<String, String>();
	List<Object> syncList = Collections.synchronizedList(new ArrayList<Object>());
	long mem = 0;
  	int k =1;
	
  	 for(int i = 0; i< 100; i++) {
  		 

         k++;
         syncList.add (new Object());
         mem =  mem +  displayMemory();
  		 
  	 }
		mem =mem/k;
	 	results.put("memStat",""+mem/k);	
	 	results.put("instStat",""+ k);
	
	  return results;	

			}

	
	public static long displayMemory() {
	    Runtime r=Runtime.getRuntime();
	    r.gc();
	    r.gc(); // gc sweep one is not enough
	   return r.totalMemory()-r.freeMemory();
	}

}

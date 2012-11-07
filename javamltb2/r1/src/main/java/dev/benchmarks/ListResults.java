package dev.benchmarks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;


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



public class ListResults implements Callable<HashMap<String, HashMap<String, Long>>> {
	
	final HashMap<String, HashMap<String, Long>> resultsMap = new HashMap<String, HashMap<String, Long>>();

	@Override
	public HashMap<String, HashMap<String, Long>> call() {
		
		resultsMap.put("copyOnWriteArray", copyOnWriteArrayListResult() );
		
		resultsMap.put("synchronizedList", copyOnWriteArrayListResult() );
	
		
	return resultsMap;
	}
	
	
	public HashMap<String, Long> copyOnWriteArrayListResult() {
		
	final HashMap<String, Long> results = new HashMap<String, Long>();
	final CopyOnWriteArrayList<Object> syncList = new CopyOnWriteArrayList<Object>();
	long mem = 0;
  	int k =1;
	
  	 for(int i = 0; i< 100; i++) {
  		 

         k++;
         syncList.add (new Object());
         mem =  mem +  displayMemory();
  		 
  	 }
	 	results.put("memStat",mem/k);	
	 	results.put("instStat",(long) k);
	
	  return results;	

			}
	
	
	public HashMap<String, Long> synchronizedListResult() {
	final HashMap<String, Long> results = new HashMap<String, Long>();
	List<Object> syncList = Collections.synchronizedList(new ArrayList<Object>());
	long mem = 0;
  	int k =1;
	
  	 for(int i = 0; i< 100; i++) {
  		 

         k++;
         syncList.add (new Object());
         mem =  mem +  displayMemory();
  		 
  	 }
	 	results.put("memStat",mem/k);	
	 	results.put("instStat",(long) k);
	
	  return results;	

			}

	
	public static long displayMemory() {
	    Runtime r=Runtime.getRuntime();
	    r.gc();
	    r.gc(); // gc sweep one is not enough
	   return r.totalMemory()-r.freeMemory();
	}


}

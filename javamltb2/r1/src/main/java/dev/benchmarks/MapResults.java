package dev.benchmarks;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/*   This class obtains the Map Benchmark Results
 * 
 *  @ resultsMap  container for list results stats
 * 
 *  @ hashMapResult() runs loop for HashMap
 *  
 *  @ concurrentHashMapResult() runs loop for ConcurrentHashMap
 * 
 *  @ hashtableResult() runs loop for HashTable
 *  
 *  @ displayMemory() obtains memory footpring of collection in loop
 * 
 */



public class MapResults implements Callable<HashMap<String, HashMap<String, Long>>>  {
	

	final HashMap<String, HashMap<String, Long>> resultsMap = new HashMap<String, HashMap<String, Long>>();

	
	
	@Override
	public HashMap<String, HashMap<String, Long>> call() {
		
		resultsMap.put("hashMap", hashMapResult() );
		resultsMap.put("concurrentMap", concurrentHashMapResult() );
		resultsMap.put("hashTable", hashtableResult());

		return resultsMap;
		}

	
	public HashMap<String, Long> hashMapResult() {
	final HashMap<String, Long> results = new HashMap<String, Long>();
  	final Map<Object,Object> testMap = new HashMap<Object, Object>();// unsynched map
  	long mem = 0;
  	int k =1;
  	
  	  for(int i = 0; i< 100; i++) {

	            k++;
	            testMap.put(new Object(),new Object());
	            mem =  mem +  displayMemory();

	            }
			 	results.put("memStat",mem/k);	
			 	results.put("instStat",(long) k);
	
		 
	  return results;	

			}
	
	
	public HashMap<String, Long> concurrentHashMapResult() {
	final HashMap<String, Long> results = new HashMap<String, Long>();
	final Map<Object,Object> testSync = new ConcurrentHashMap<Object,Object>();
  	long mem = 0;
  	int k =1;
  	
  	  for(int i = 0; i< 100; i++) {

	            k++;
	            testSync.put(new Object(),new Object());
	            mem =  mem +  displayMemory();

	            }
			 	results.put("memStat",mem/k);	
			 	results.put("instStat",(long) k);

		 
	  return results;	

			}
	 
	
		public HashMap<String, Long> hashtableResult() {
			final HashMap<String, Long> results = new HashMap<String, Long>();
			 final Hashtable<Object,Object> table = new Hashtable<Object, Object>();
		  	long mem = 0;
		  	int k =1;
		  	
		  	  for(int i = 0; i< 100; i++) {

			            k++;
			            table.put(new Object(),new Object());
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

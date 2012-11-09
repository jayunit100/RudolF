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



public class MapResults implements Callable<HashMap<String, HashMap<String, String>>>  {
	
	private final  ExecutorService threadExecutor = Executors.newCachedThreadPool();
	private final HashMap<String, HashMap<String, String>> resultsMap = new HashMap<String, HashMap<String, String>>();
	private final HashMap<String, String> dumpResults = new  HashMap<String, String>();
	private final Dumper dumpTask;
	private  Future<String> mapDumpTask;



	public MapResults(final Dumper dumpTask) {
		super();
		this.dumpTask = dumpTask;
	}


	@Override
	public HashMap<String, HashMap<String, String>> call() {
		
		resultsMap.put("hashMap", hashMapResult());
		resultsMap.put("concurrentMap", concurrentHashMapResult() );
		resultsMap.put("hashTable", hashtableResult());
		
		try {
			dumpResults.put("single" ,mapDumpTask.get());
			
		} catch (InterruptedException e) {e.printStackTrace();
		} catch (ExecutionException e) {e.printStackTrace();
		}
		resultsMap.put("dumpReport", dumpResults);
		threadExecutor.shutdown();
		return resultsMap;
		}

	
	public HashMap<String, String> hashMapResult() {
	final HashMap<String, String> results = new HashMap<String,String>();
  	final Map<Object,Object> testMap = new HashMap<Object, Object>();// unsynched map
  	long mem = 0;
  	int k =1;
  	
  	mapDumpTask = threadExecutor.submit(dumpTask);
  	
  	  for(int i = 0; i< 100; i++) {

	            k++;
	            testMap.put(new Object(),new Object());
	            mem =  mem +  displayMemory();

	            }
  	  			mem =mem/k;
			 	results.put("memStat",""+mem/k);	
			 	results.put("instStat",""+ k);
	
		 
	  return results;	

			}
	
	
	public HashMap<String, String> concurrentHashMapResult() {
	final HashMap<String, String> results = new HashMap<String, String>();
	final Map<Object,Object> testSync = new ConcurrentHashMap<Object,Object>();
  	long mem = 0;
  	int k =1;
  	
  	  for(int i = 0; i< 100; i++) {

	            k++;
	            testSync.put(new Object(),new Object());
	            mem =  mem +  displayMemory();

	            }
		mem =mem/k;
	 	results.put("memStat",""+mem/k);	
	 	results.put("instStat",""+ k);

		 
	  return results;	

			}
	 
	
		public HashMap<String,String> hashtableResult() {
			final HashMap<String, String> results = new HashMap<String, String>();
			 final Hashtable<Object,Object> table = new Hashtable<Object, Object>();
		  	long mem = 0;
		  	int k =1;
		  	
		  	  for(int i = 0; i< 100; i++) {

			            k++;
			            table.put(new Object(),new Object());
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

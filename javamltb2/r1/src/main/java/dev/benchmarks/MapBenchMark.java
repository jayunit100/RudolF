package dev.benchmarks;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapBenchMark extends BenchMarkBase {
	
	/**   This class obtains the  new Benchmark results for the java.util.concurrent.ConcurrentHashMap 
	 *   compared to the standard Maps found in the  java.util.* Collections package. 
	 *   
	 *   @getMapResults() compares the cost for synchronization for the 1.5  synchronized Maps
	 *   compared to the Java 1.4  synchronized HashTable. These are compared to the 
	 *   unsynchronized HashMap.
	 *   
	 *   @getMapMemoryResults compares the memory use for the 1.5  synchronized Maps
	 *   compared to the Java 1.4  synchronized HashTable. These are compared to the 
	 *   unsynchronized HashMap.
	 *   
	 *   in this class we learn that the 1.5  synchronized Maps reduce the cost of synchronization over 
	 *   HashTable 
	 *   
	 */

	

	public void getMapResults() {
		
		start=System.currentTimeMillis();
		
		Map hashMap;
		
		Hashtable  hashTable;
		
		Map syncMap;	
		
		for(int i = 0 ; i < 100 ; i++){
			long start=0L;
			
			start=System.currentTimeMillis();
			hashMap =  new HashMap();
			populateMap(hashMap);
			System.out.println("HashMap "+(System.currentTimeMillis()-start));
			
			start=System.currentTimeMillis();
			syncMap = new ConcurrentHashMap();
			populateMap(syncMap);
			System.out.println("ConcurrentHashMap "+(System.currentTimeMillis()-start));
			
			
			
			start=System.currentTimeMillis();
			hashTable = new Hashtable();
			populateMap(hashTable);
			System.out.println("Hashtable "+(System.currentTimeMillis()-start));
		
		
			}
	
	
	}
	
	// test the memory footprint of the maps
	// results seem  show memeory footprint of HashMap is significantly  better than  synchronized maps
	public void getMapMemoryResults() {
		// test maps
		Map hashMap;		
		Hashtable  hashTable;
		Map syncMap;
		
		// get System free memory
		long mem = displayMemoryRuntime();
		System.out.println("displayMemoryRuntime " + mem + " bytes"); 
		hashMap =  new HashMap();		
		populateMap(hashMap);
		// get memory footprint of map
		mem = displayMemoryRuntime() - mem;		 
		System.out.println("hashMap " + mem + " bytes"); 
		hashMap.clear();
		
		// repeat for ConcurrentHashMap
		mem = displayMemoryRuntime(); 
		syncMap = new ConcurrentHashMap();
		populateMap(syncMap);		 
		mem = displayMemoryRuntime() - mem;		 
		System.out.println("ConcurrentHashMap " + mem + " bytes"); 
		syncMap.clear();
		
		// repeat for hashTable
		mem = displayMemoryRuntime();
		hashTable = new Hashtable();
		populateMap(hashTable);
		mem = displayMemoryRuntime() - mem;	
		System.out.println("Hashtable " + mem + " bytes"); 
		hashTable.clear();
		 
		
		
	}
	
	
	
	
	
}

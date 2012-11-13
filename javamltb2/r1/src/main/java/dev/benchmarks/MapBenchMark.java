package dev.benchmarks;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapBenchMark extends BenchMarkBase {
	
	/**   This class obtains the  new Benchmark results for the java.util.concurrent.ConcurrentHashMap 
	 *   compared to the standard Maps found in the  java.util.* Collections package. 
	 */

	

	public void getMapResults() {
		
		start=System.currentTimeMillis();
		
		Map hashMap;
		
		Hashtable  hashTable;
		
		Map syncMap;	
		
		for(int i = 0 ; i < 10 ; i++){
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
	
	
}

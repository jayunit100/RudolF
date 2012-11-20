package dev.benchmarks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

/**  This class is the SuperClass for the Bench Mark Classes
 * 
 *	 collect common methods and variables here
 * 
 */



public class BenchMarkBase {
	static int SIZE = 40;
	long start=0L;

	public static void populateMap(Map m){
		for(int i = 0 ; i < SIZE; i++){
			m.put("a string", "a string");
		}
	}
	
	public static void populate(Collection c){
		for(int i = 0 ; i < SIZE; i++){
			c.add("a string");
		}
	}

	protected void getFreeMemory(int i)  {
		System.out.println(i+": "+Runtime.getRuntime().freeMemory());
	}
	
	// get the memory footprint of the map at a point in the loop
	protected  static long displayMemoryRuntime() {
	    Runtime r=Runtime.getRuntime();
	    System.gc();
	    r.gc();
	    r.gc();	//Two  gc sweep required. One is not enough because some objects possibly will over-ride finalize() 
	    		//and if as a result the object becomes accessible by a live thread it will not be garbage collected. 
	    		//As this occurred in a finalizer method the object still needs to be garbage collected, however in the 
	    		//JMM the finalize method will not be called again, so we cover for this with the second gc() call.
	   return r.totalMemory() -  r.freeMemory();
	}
}


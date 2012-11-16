package dev;

import dev.benchmarks.ListBenchMark;
import dev.benchmarks.MapBenchMark;

/**  
 *  This class is the entry point for the benchmarking and thread dump code
 *  
 *  
 *  it reports the results (Strings) for  benchmarks as a simple screen dump
 *  
 *  @ ListBenchMark runs the simple ListBenchMarks
 *  @ MapBenchMark  Bench Marks for Java Collection Data Structures 
 * 
 * The Classes show the  memory footprint for implementations of 
 * java.util.concurrent.ConcurrentHashMap and java.util.concurrent.CopyOnWriteArrayList compared to the 
 * standard maps and lists in java.util.*
 * 
 * From the JavaDoc for "java.util.concurrent" 
 * 
 * "CopyOnWriteArrayList is  a "thread-safe variant of ArrayList in which all mutative 
 * operations (add, set, and so on) are implemented by making a fresh copy of the array." 
 * The collection internally copies its contents over to a new array upon any modification, so readers 
 * accessing the contents of the array incur no synchronization costs (because they're never operating on mutable data)."
 * 
 * The List BenchMarks compare the cost of implementing CopyOnWriteArrayList to Collections.synchronizedList
 * 
 * Map hosts a subtle concurrency bug. When a Map is accessed from multiple threads, the use of either containsKey() or get() 
 * to find out whether a given key is present before storing the key/value pair allows for another thread 
 * to seize control of the Map. The  lock is acquired at the start of get(), then released before the lock can be acquired 
 * again, in the call to put(). 
 * The result is a race condition between the two threads, the outcome will be different based on which runs first. Worse 
 * if two threads call a method at concurrently, each will test and put, losing the first thread's value in the process
 * 
 * ConcurrentMap is an easy solution. The map BenchMarks compare the cost of implementing java.util.concurrent.ConcurrentMap
 * to the standard Maps in java.util.*
 */
public class JvmResearch {


	public static void main(String[] args) throws Exception {


		ListBenchMark listBenchMark = new ListBenchMark();
		
		listBenchMark.runSimpleArrayList();
		
		listBenchMark.runSimpleVectorTest();
		
		listBenchMark.runSynchronisedListComparisonTest();
		
		MapBenchMark mapBenchMark  = new   MapBenchMark();
		
		mapBenchMark.getMapResults();
	}
}

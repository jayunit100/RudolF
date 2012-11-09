package dev;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dev.benchmarks.BenchMarkRunner;
import dev.dump.Dumper;

/**  
 *  This class is the entry point for the benchmarking and thread dump code
 *  reports results as a simple screen dump
 *  
 *  @ dumpTask class dumps status on all threads at a specified time in the program 
 *    using  java.util.concurrent.Future
 * 
 *  @ benchMarkTask  Bench Marks for Java Collection Data Structures
 *  
 * This class aggregates data from dev.benchmarks.BenchMarkRunner and dev.dump.Dumper
 * 
 * String from dev.benchmarks.BenchMarkRunner shows memory footprint for concurrent implementations of 
 * java.util.concurrent.ConcurrentHashMap and java.util.concurrent.CopyOnWriteArrayList compared to the 
 * standard maps and lists in java.util.*
 * 
 * From the JavaDoc CopyOnWriteArrayList is  a "thread-safe variant of ArrayList in which all mutative 
 * operations (add, set, and so on) are implemented by making a fresh copy of the array." 
 * The collection internally copies its contents over to a new array upon any modification, so readers 
 * accessing the contents of the array incur no synchronization costs (because they're never operating on mutable data).
 * 
 * The List BenchMarks compare the cost of implementing CopyOnWriteArrayList to Collections.synchronizedList
 * 
 * Map hosts a subtle concurrency bug. When a Map is accessed from multiple threads, the use of either containsKey() or get() 
 * to find out whether a given key is present before storing the key/value pair allows for another thread 
 * to seize control of the Map. The  lock is acquired at the start of get(), then released before the lock can be acquired again, in the call to put(). 
 * The result is a race condition between the two threads, the outcome will be different based on which runs first. Worse 
 * if two threads call a method at concurrently, each will test and put, losing the first thread's value in the process
 * 
 * ConcurrentMap is an easy solution. The map BenchMarks compare the cost of implementing java.util.concurrent.ConcurrentMap
 * to the standard Maps in java.util.*
 */
public class JvmResearch {


	public static void main(String[] args) throws Exception {


		// java concurrency thread container
		ExecutorService threadExecutor = Executors.newCachedThreadPool();
		
		// java concurrency Future task to obtain a thread dump
		Future<String> dumpTask;
		
		//  java concurrency Future task to run collection benchmarks
		Future<String> benchMarkTask;
		
		// method submit extends Executor.execute(java.lang.Runnable) to create and return a Future
		// gere we use it with the Dumper class to get initial thread information
		dumpTask = threadExecutor.submit(new Dumper("Initial Thread state task " ));
		
		// report initial thread information		
		System.out.println("Initial Thread dunp task result:"+dumpTask.get());
	
		System.out.println("");
		System.out.println("Please wait while running Java Collection Benchmarks may take a minute");
			// run collection benchmarks
		benchMarkTask = threadExecutor.submit(new BenchMarkRunner());
		
		// report benchMarkTask results
		System.out.println(benchMarkTask.get());
		System.out.println("");
		System.out.println("End  Java Collection Benchmarks");
		System.out.println("");

		threadExecutor.shutdown();

	}
}

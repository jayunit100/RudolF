package dev;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dev.benchmarks.BenchMarkRunner;
import dev.dump.Dumper;

/**  
 *  This class is the entry point for the benchmarking and optimization code
 * 	results will be stored in an embedded Derby database.
 * 
 * 
 *  @ dumpTask class dumps status on all threads at a specified time in the program 
 *    using  java.util.concurrent.Future
 * 
 *  @ BenchMarkRunner.getResults();  Bench Marks for Java Collection Data Structures
 *  shows memory footprint for concurrent implementations of 
 * 			java.util.concurrent.ConcurrentHashMap; 
 * 			java.util.concurrent.CopyOnWriteArrayList;
 */
public class JvmResearch {


	public static void main(String[] args) throws Exception {


		// java concurrency thread container
		ExecutorService threadExecutor = Executors.newCachedThreadPool();
		
		// java concurrency Future task to obtain a thread dump
		Future<String> dumpTask;
		
		//  java concurrency Future task to run collection benchmarks
		Future<String> benchMarkTask;
		
		// get initial thread information
		dumpTask = threadExecutor.submit(new Dumper("Initial Thread state task " ));
				
		System.out.println("Initial Thread dunp task result:"+dumpTask.get());
	
		System.out.println("");
		System.out.println("Please wait while running Java Collection Benchmarks may take a minute");
			// run collection benchmarks
		benchMarkTask = threadExecutor.submit(new BenchMarkRunner());
		System.out.println(benchMarkTask.get());
		System.out.println("");
		System.out.println("End  Java Collection Benchmarks");
		System.out.println("");

			// all done drop the database as is just system  test data for now
		threadExecutor.shutdown();

	}
}

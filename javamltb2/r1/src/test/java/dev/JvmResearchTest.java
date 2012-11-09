package dev;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import dev.benchmarks.BenchMarkRunner;
import dev.dump.Dumper;

/* 
 * ideas
 * 1 set up a resource intensive process to test JVM

 * main loop  to be investigated for the stack memory and loop optimization

 */
public class JvmResearchTest {

	

	public static void stageOneTest() throws InterruptedException, ExecutionException {

		// java concurrency thread container
		ExecutorService threadExecutor = Executors.newCachedThreadPool();
		
		// java concurrency Future task to obtain a thread dump
		Future<String> dumpTask;
		dumpTask = threadExecutor.submit(new Dumper("Initial Thread state task " ));
		System.out.println("Running Test 1 Initial Thread Dump:"+dumpTask.get());

		dumpTask = threadExecutor.submit(new Dumper("Initial Thread state task " ));
				
		System.out.println("Initial Thread dump task result:"+dumpTask.get());		

			// all done drop the database as is just system  test data for now
		threadExecutor.shutdown();

			}
	
	public static void stageTwoTest() throws InterruptedException, ExecutionException {

		// java concurrency thread container
		ExecutorService threadExecutor = Executors.newCachedThreadPool();
		Future<String> benchMarkTask;
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


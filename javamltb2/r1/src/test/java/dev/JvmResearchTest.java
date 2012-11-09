package dev;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import dev.benchmarks.BenchMarkRunner;
import dev.dump.Dumper;

/* 
 * This class contains  methods to run staged tests
 * idea is to isolate problems in the code and make then easier to identify and deal with
 * each stage will introduce more complex code that if fails by knowing the stage 
 * debugging will be quicker
 */
public class JvmResearchTest {

	
// for now this just aggregates the threads while the system is idle
	public static void stageOneTest() throws InterruptedException, ExecutionException {

		// java concurrency thread container
		ExecutorService threadExecutor = Executors.newCachedThreadPool();
		
		// java concurrency Future task to obtain a thread dump
		Future<String> dumpTask;
		dumpTask = threadExecutor.submit(new Dumper("Initial Thread state task " ));
		System.out.println("Running Test 1 Initial Thread Dump for idle system:");

		dumpTask = threadExecutor.submit(new Dumper("Initial Thread state task " ));
				
		System.out.println("Initial Thread dump task result:"+"\n"+dumpTask.get());		
		System.out.println("End Test 1 Initial Thread Dump for idle system");
		System.out.println("End Test 1 "+"\n");
		// all done drop the database as is just system  test data for now
		threadExecutor.shutdown();

			}
	
	public static void stageTwoTest() throws InterruptedException, ExecutionException {

		// java concurrency thread container
		ExecutorService threadExecutor = Executors.newCachedThreadPool();
		Future<String> benchMarkTask;
		System.out.println("Running Test 2 Java Collection Benchmarks with Thread Dump:"+"\n");
		System.out.println("Please wait while running Java Collection Benchmarks may take a minute"+"\n");
		// run collection benchmarks
		benchMarkTask = threadExecutor.submit(new BenchMarkRunner());	
		System.out.println(benchMarkTask.get());
		System.out.println("");
		System.out.println("End  Java Collection Benchmarks with Thread Dump");
		System.out.println("");
		System.out.println("End Test 2 "+"\n");
		System.out.println("End Tests "+"\n");
			// all done drop the database as is just system  test data for now
		threadExecutor.shutdown();

			}
	
	
	
	
	}


package dev;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dev.derbyutils.DerbyTask;
import dev.derbyutils.DerbyUtils;
import dev.dump.Dumper;
import dev.testobjects.StructureWrapper;

/*  This class is the entry point for the benchmarking and optimization code
 * 	results will be stored in an embedded Derby database.
 * 
 *  @ structList an array of simple structures use for memory tests by sun.misc.Unsafe.class
 * 
 *  @ Franken class is a wrapper class to obtain an instance of sun.misc.Unsafe.class 
 * 
 *  @ dumpTask class dumps ststus on all threads at a specified time in the program 
 *    using  java.util.concurrent.Future
 * 
 */
public class JvmResearch {

	// initial testing for Unsafe behaviour
	private static final List<Object> structList;

	// static initialiser
	static {
		structList = Arrays.asList(
				new StructureWrapper().new MyStructureEmpty(),
				new StructureWrapper().new MyStructureOneInt(),
				new StructureWrapper().new MyStructureTwoInt());
	}
	// for derby sys independent path
	private static String sep = File.separator;
	// derby object id
	private static Integer k;

	public static void main(String[] args) throws Exception {

		/* results database */
		DerbyUtils.makeDerby("benchMarkResults");
		// thread container
		ExecutorService threadExecutor = Executors.newCachedThreadPool();
		Future<String> dumpTask;

		k = 1;// count the loops if needed
		try {
			for (Object obj : structList) {
				// pass thread an object to be investigated or manipulated
				DerbyTask derbyTask = new DerbyTask(obj, k);
				// pause for a while as derby is slow
				threadExecutor.execute(derbyTask);
				
				dumpTask = threadExecutor.submit (new  Dumper("structList memory task "+k));
				
				System.out.println(dumpTask.get());
				

				try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
				}
				;
				System.out.println("insert: " + (k++) + ", completed");

			}// end loop
			
			
			
			

		} finally {

			// all done drop the database as is just junk test data for now
			System.out.println("report Derby");
			System.out.println("dropDerby");
			threadExecutor.shutdown();

			DerbyUtils.dropDerby("benchMarkResults");
		}

	}
}

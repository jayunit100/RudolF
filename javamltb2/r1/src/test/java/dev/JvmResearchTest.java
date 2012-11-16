package dev;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import dev.benchmarks.ListBenchMark;
import dev.benchmarks.MapBenchMark;
import dev.dump.Dumper;

/* 
 * This class contains  methods to run staged tests
 * idea is to isolate problems in the code and make then easier to identify and deal with
 * each stage will introduce more complex code that if fails by knowing the stage 
 * debugging will be quicker
 */
public class JvmResearchTest {

	

	
	public static void stageOneTestNewCode() throws InterruptedException, ExecutionException {


		System.out.println("Stage 1 Tests Bench Marks");
		
		
		ListBenchMark listBenchMark = new ListBenchMark();
		
		listBenchMark.runSimpleArrayList();
		
		listBenchMark.runSimpleVectorTest();
		
		listBenchMark.runSynchronisedListComparisonTest();
		
		
		MapBenchMark mapBenchMark  = new   MapBenchMark();
		
		mapBenchMark.getMapResults();

			}
	
	
	
	public static void stageTwoTestNewCode() throws InterruptedException, ExecutionException {


		System.out.println("Stage 2 Tests Main Profile");
		
		JVMProfiler.main(new String[] { "arg1" });
	

			}
	

	public static void stageThreeTestNewCode() throws Exception {


		System.out.println("Stage 2 Tests Heap Profile");
		
		HeapProfiler.main(new String[] { "arg1" });
	

			}  
	
	
	}


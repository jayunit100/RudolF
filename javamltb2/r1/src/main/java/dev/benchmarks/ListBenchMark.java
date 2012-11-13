package dev.benchmarks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**   This class obtains the  Benchmark results for the Lists
 *  
 * 
 */

@SuppressWarnings("unchecked" )
public class ListBenchMark extends BenchMarkBase {
	

	
	public ListBenchMark() {
		super();
		
	}


	/*
	 * In this very simple, single threaded example, we will monitor the increasing 
	 * memory load taken up by a simple loop which creates a list of lists, where each list has a string in it.  
	 * 
	 * The data structure created in main() memory is like this: 
	 * [ ["A string"] ["A string"] ["A string"] ...] 
	 * 
	 * However, interestingly, even though the "A string" is exactly 8 bytes, each memory iteration increase the
	 * heap size by 20 bytes.  We can thus learn about the JVM here, in particular: 
	 */
	public void runSimpleArrayList() {

	@SuppressWarnings("rawtypes")
	List myListHolder = new ArrayList();	
	
	
	for(int i = 0 ; i < SIZE; i++){
		List<String> l = new ArrayList<String>();
		l.add("A string");
		myListHolder.add(l);
	//	System.gc(); //<-- Auxillary question: Nigel - why is this call necessary to get a clear value for "freeMemory"?

		System.out.println(i+": "+Runtime.getRuntime().freeMemory());
		
		}
	}
	
	
	/*
	 * What happens when the JVM attempts to synchronize() a method call? 
	 * How long does this take? How many operations does it involve?
	 * 
	 *  The vector is a synchronized class shows the effects of the extra operations
	 */	
	public void runSimpleVectorTest() {
	
	
		for(int i = 0 ; i < 10 ; i++){

			start=System.currentTimeMillis();
			Vector v = new Vector(SIZE);
			populate(v);
			System.out.println(System.currentTimeMillis()-start);
	
			start=System.currentTimeMillis();
			List l = new ArrayList(SIZE);
			populate(l);
			System.out.println(System.currentTimeMillis()-start);
			System.out.println("--");
				}	
			
		}
	
	
	
	public void runSynchronisedListComparisonTest() {
		@SuppressWarnings("rawtypes")
		List myListHolder = new ArrayList();	
		CopyOnWriteArrayList syncList = new CopyOnWriteArrayList();
		
		
		for(int i = 0 ; i < SIZE; i++){
			List<String> l = new ArrayList<String>();
			l.add("A string");
			myListHolder.add(l);
			
			System.gc(); //<-- Auxillary question: Nigel - why is this call necessary to get a clear value for "freeMemory"?
								
			System.out.println(i+": "+""+Runtime.getRuntime().freeMemory()+"\n");
					
			}
		
		
		for(int i = 0 ; i < SIZE; i++){
			List<String> l = new ArrayList<String>();
			l.add("A string");
			syncList.add(l);
			
			System.gc(); //<-- Auxillary question: Nigel - why is this call necessary to get a clear value for "freeMemory"?
		
						
			System.out.println(i+": "+""+Runtime.getRuntime().freeMemory()+"\n");
	
			
			}
		

		
		}
	

	
	}


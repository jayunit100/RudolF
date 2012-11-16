package dev.benchmarks;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**   This class obtains the  Benchmark results for the Lists
 *    We look at memory usage by simple collections
 *    @runSimpleArrayList() looks at memory model for ArrayList
 * 	  @runSimpleVectorTest() looks at the cost for Vectors synchronization
 * 	  compared to ArrayList
 * 	  @runSynchronisedListComparisonTest() looks at the cost for synchronization
 * 	  in the new java 1.5 synchronized collections.
 * 
 * 	  in this class we learn that the 1.5  synchronized collections
 * 	  reduce the cost of synchronization over Vector
 * 
 *   in this class we learn about the big cost to runtime performance
 *   of a call to System.gc()
 */

@SuppressWarnings("unchecked" )
public class ListBenchMark extends BenchMarkBase {
	
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
	 *  The vector is a synchronized class shows the effects of the extra operations, however 
	 *  in a method like this , may be inlined and these extra operations can be removed
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
	
	
	/*
	 * Shows cost of synchronized collection CopyOnWriteArrayList
	 *  to ArrayList
	 */	
		
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


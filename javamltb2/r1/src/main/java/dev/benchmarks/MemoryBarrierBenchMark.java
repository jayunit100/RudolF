package dev.benchmarks;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;


/**   This class obtains the  Benchmark results for the JMM Memory Barrier
 *	  Memory barriers are used to achieve visibility. A mutex is a lock. 
 *	  A program can behave non-deterministically when memory operations on shared mutable state are re-ordered. 
 *    A thread may write values that become visible to another thread in ways that are inconsistent 
 *    with the order in which they were written. A properly placed memory barrier prevents this.
 *     
 *    In the Java Memory Model a volatile field has a store barrier inserted after a write to it 
 *    and a load barrier inserted before a read of it.  Qualified final fields of a class have a 
 *    store barrier inserted after their initialization.
 *     
 *    Atomic instructions, are effectively a full barrier as they lock the memory sub-system to 
 *    perform an operation and have guaranteed total order, even across CPUs.  Software locks usually 
 *    employ memory barriers, or atomic instructions, to achieve visibility and preserve program order.
 *
 *		@counterAtomic AtomicInteger field for comparison with non atomic fields
 *		@compareInLineToBarrier() memory barrier  set up by a method call compared to a  no barrier operation
 *
 */



public class MemoryBarrierBenchMark {
	
	// Atomic operations are ones which manipulate memory in a way that appears indivisible: 
	// No thread can observe the operation half-complete. This is the simplest example of a memory barrier
	AtomicInteger counterAtomic = new AtomicInteger(0);
	
	private int counter;
	private 	Vector vBarrier = new Vector();
	private 	Vector vInline = new Vector();
	private static final String fixed = "4byt";
	
	
	
	
	synchronized void inc(){ counter += 1; }
	
	
	void barrier() {
		
		vBarrier.add(fixed );
		
	}
	

	
	public void compareSynchronizedToBarrier() {
		long a = System.currentTimeMillis();
		
		   for (int i=0; i<1000000; i++)   {
			   counter += 1;
		     }
		
		   System.out.println("Synchronized Method "+(System.currentTimeMillis()-a)+"s");
		   
		   a = System.currentTimeMillis();
		   
		    for(int i = 0; i < 1000000; i++) counterAtomic.incrementAndGet();
		    		    
		    System.out.println("AtomicInteger Method "+(System.currentTimeMillis()-a)+"s");
        }
	
	// tests a thread local memory barrier  set up by a method call compared to a  no barrier operation
	public void compareInLineToBarrier() {
		long a = System.currentTimeMillis();
		
		   for (int i=0; i<1000000; i++)   {
			   vInline.add(fixed);
		     }
		
		   System.out.println("InLine Method "+(System.currentTimeMillis()-a)+"s");
		   
		   a = System.currentTimeMillis();
		   
		    for(int i = 0; i < 1000000; i++) barrier();
		    		    
		    System.out.println("memory barrier "+(System.currentTimeMillis()-a)+"s");
        }
		
		
	}
	
	


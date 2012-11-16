package dev.benchmarks;

import java.util.concurrent.atomic.AtomicInteger;


/**   This class obtains the  Benchmark results for the MemoryBarrier
 *	  Memory barriers are used to achieve an important element of 
 *	  concurrent programming called visibility. A program can behave non-deterministically 
 *    when memory operations on shared mutable state are re-ordered. It is possible for a 
 *    thread to write values that become visible to another thread in ways that are inconsistent 
 *    with the order in which they were written. A properly placed memory barrier prevents this. 
 *    
 *
 *		@counterAtomic AtomicInteger field for comparison with non atomic fields
 *
 */



public class MemoryBarrierBenchMark {
	
	
	AtomicInteger counterAtomic = new AtomicInteger(0);
	int counter;
	
	
	
	synchronized void inc(){ counter += 1; }
	
	
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
		
	}
	




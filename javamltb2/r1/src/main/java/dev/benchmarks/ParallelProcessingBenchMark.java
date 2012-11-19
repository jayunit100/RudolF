package dev.benchmarks;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* *Simple class to test memory barrriers in the JMM.
 * JMM explained for newbies very well here http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html
 *  To have a memory barrier you must set up a lock on an object a "prize variable" and the Threads will  
 *  compete for access to this "prize'
 * 
 * Java handles this well with synchronized, but there are underlying consistency issues in the hardware
 * see this excellent article http://www.cl.cam.ac.uk/~pes20/weakmemory/
 * 
 * see this article for the barrier schema http://g.oswego.edu/dl/jmm/cookbook.html
 * 
 * so we can run tests where Threads compete for access. We can look at fine and course locks and 
 * how they impact JVM performance
 * 
 * 
 * 
 */

public class ParallelProcessingBenchMark {
	

	static AtomicInteger atom = new AtomicInteger();
	volatile int volt = 0;
	static int NUM_TESTS =10;
	static int threadId =0;
	final static CountDownLatch start = new CountDownLatch(1);
    final static CountDownLatch end = new CountDownLatch(NUM_TESTS);
	
	// simplest example, need to extend for thread competition
	
	public  void simpleLockTest() throws InterruptedException {
		
		
		for(int i = 0; i < NUM_TESTS; i++){
			
			new Thread(new Runnable(){
				
				
				private final ReadWriteLock rwl = new ReentrantReadWriteLock();

				@Override
				public void run() {
					Random rand= new Random();
					int min = 700, max = 10000;
					int randomNum = rand.nextInt(max - min + 1) + min;
				// try and throw threads of sequential access	
					 try {
						Thread.sleep(randomNum);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					Lock rl = rwl.writeLock();
					rl.lock();
					atom.incrementAndGet();
					rl.unlock();
					
					Lock wl = rwl.readLock();
					wl.lock();
					System.out.println("threadId "+threadId +"lockOblect state "+atom.get());
					
					wl.unlock();
					threadId++;
					
						}
				}).start();

				}
			// start the threads and wait for finish
				start.countDown();
	        try {
	            end.await();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		}
	
	
	

	

}
	
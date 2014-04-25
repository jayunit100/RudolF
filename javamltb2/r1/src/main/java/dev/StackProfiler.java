package dev;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;


/**   This class attempts to profile the Java Stack
 * 
 * 	@CustomThread This class generates types of this thread, that then call a recursive method
 *  to repeatedly call the stack to generate a stack overflow
 * 
 * 	@minRecursion statistic for the minimum number of recursions before a stack overflow 
 * 
 * 	@minRecursion statistic for the maximum number of recursions before a stack overflow 
 *    
 *  @gcbeans  Collection of java.lang.management.* GarbageCollectorMXBeans
 *  
 * 	@getGcSum() generates a count statistic of the number of times the gc is called
 * 
 *  from this class we can learn, the number of method calls the stack can handle and
 *  the number of threads the System runtime can generate before an out of memory error
 * 	 
 * 
 */




public class StackProfiler implements Runnable {
	
	
	 //recursion statistics
	 int minRecursion = Integer.MAX_VALUE;
	 int maxRecursion = 0;
	 
	 //GarbageCollectorMXBeans implement the management interface for the garbage collection
	 List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();	
	
	public static void main(String[] args) throws Exception {				
		StackProfiler threadFactory = new StackProfiler();
		threadFactory.run();
		
	}
	
	// generate threads to try and create a Stack overflow error
    public void run() {
        int i=0;
        long start = System.currentTimeMillis();
        try {
            for (;;i++) {
                Thread a = new CustomThread(String.valueOf(i));
                a.setDaemon(true);
                a.start();
                System.out.println(i+": GC "+ getGcSum());
            }
        } catch(OutOfMemoryError e) {
            // we create threads until we run out of memory
        }
        long duration = System.currentTimeMillis() - start;
        float tps = i / (duration / 1000.0f);
        System.out.println("Max. threads: "+ (i-1));
        System.out.println("Max. recursions: "+ maxRecursion);
        System.out.println("Min. recursions: "+ minRecursion);
        System.out.println("Threads per second: "+ tps);
    }
	
	// count the gc calls
    private int getGcSum() {
        int sum = 0;
        for (GarbageCollectorMXBean mb : gcbeans) {
            sum += mb.getCollectionCount();
        }
        return sum;
    }
   
    
    // custom thread that uses a recursive function to repeatedly call the stack to generate a Stack overflow
 public class CustomThread extends Thread {
        
        protected CustomThread(String name) {
            super(name);
        }

        public void run() {
        	stackRecurser(0);
        }
      
        // this recursion function  calls the stack, with the intention of blowing it out
        private void stackRecurser(int i) {
            try {
                i++;
                stackRecurser(i);
            } catch(StackOverflowError e) {
                System.out.print("number recursions"+ this.getName()+": "+ i +"\n");
                
                synchronized (StackProfiler.this) {
                	//catch smallest value
                    minRecursion = Math.min(minRecursion, i);
                    // catch largest value
                    maxRecursion = Math.max(maxRecursion, i);
                }
                try {
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException e1) {
                    System.out.print("interupted");
                }
            }
        }
    }
    
    
    
    

}

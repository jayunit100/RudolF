package dev;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 *  Basic class to demonstrate use of java.lang.management package.
 *  These class are Beans that provide access to the various runtime
 *  components of the JVM. 
 * 
 * 
 * @GarbageCollectorMXBean  interface for the garbage collection of the JVM
 * 
 * @MemoryPoolMXBean management interface for a memory pool
 * 
 * @ThreadMXBean provides support for monitoring thread contention and thread CPU time
 * 
 * @RuntimeMXBean interface for the runtime system of the JVM. The JVM has a single 
 * instance of the implementation class of this interface
 * 
 * @OperatingSystemMXBean interface for the operating system on which the Java virtual 
 * machine is running
 *
 */



public class JVMProfiler {
	public static void main(String[] args) {
		
		// get the OperatingSystemMXBean "singelton"
		//OperatingSystemMXBean interface defines  methods for accessing system properties
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		
		for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods())  {
		method.setAccessible(true);
		if (method.getName().startsWith("get")&& Modifier.isPublic(method.getModifiers())) {
		Object value;
		try {
		value = method.invoke(operatingSystemMXBean); 
		}  catch (Exception e) { value = e;
		System.out.println( e.getLocalizedMessage());
		}
		
		// lets see what OperatingSystemMXBean does
		System.out.println("\t"+method.getName() + " = " + value);
		}
		}// end for
		
		// get the RuntimeMXBean "singelton"
		RuntimeMXBean mxbean = ManagementFactory.getRuntimeMXBean();	
		// get some info for basic runtime parameters
		System.out.println(mxbean.getVmVendor());
		// comment out temp for less cluttered output
//		System.out.println(mxbean.getBootClassPath());	
//		System.out.println(mxbean.getSystemProperties());
//		System.out.println(mxbean.getLibraryPath());	
		
		// get the ThreadMXBean ony call the simplest parameters
		// can do a lot more here 
		//JVM may support monitoring of object monitor usage and ownable synchronizer usage. 
		//The getThreadInfo and dumpAllThreads methods can be used to obtain the thread stack 
		//trace and synchronization information including which lock a thread is blocked to 
		//acquire or waiting on and which locks the thread currently owns.
		ThreadMXBean tb = ManagementFactory.getThreadMXBean();
		System.out.println(tb.getThreadCount());
		System.out.println(tb.getPeakThreadCount());	
		
		
		
	     List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
	    // One bean is returned for each memory pool provided, so we get the list of pools
         for (MemoryPoolMXBean pool : pools) {
           MemoryUsage peak = pool.getPeakUsage();
           System.out.printf("Peak %s memory used: %,d%n", pool.getName(), peak.getUsed());          
           System.out.printf("Peak %s memory reserved: %,d%n", pool.getName(), peak.getCommitted());
         }
         
         
         List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();	
         // can do more for example getCollectionCount gives the total number of collections that have occurred
         for (GarbageCollectorMXBean gc : gcbeans) {        	 
        	 System.out.println("gc name"+ gc.getName());       	 
        	 for (String s : gc.getMemoryPoolNames())  System.out.println(s);        	       	 
         }
		

	}

}

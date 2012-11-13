package dev;

import java.lang.instrument.Instrumentation;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;

public class JVMProfiler {

	public static void main(String[] args) {
		
		RuntimeMXBean mxbean = ManagementFactory.getRuntimeMXBean();	
		System.out.println(mxbean.getVmVendor());
		System.out.println(mxbean.getBootClassPath());
		System.out.println(mxbean.getSystemProperties());
		System.out.println(mxbean.getLibraryPath());	
		ThreadMXBean tb = ManagementFactory.getThreadMXBean();
		System.out.println(tb.getThreadCount());
		System.out.println(tb.getPeakThreadCount());		
	     List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
         for (MemoryPoolMXBean pool : pools) {
           MemoryUsage peak = pool.getPeakUsage();
           System.out.printf("Peak %s memory used: %,d%n", pool.getName(), peak.getUsed());
           
           System.out.printf("Peak %s memory reserved: %,d%n", pool.getName(), peak.getCommitted());
         }

         List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();
		
         for (GarbageCollectorMXBean gc : gcbeans) {
        	 
        	 System.out.println("gc name"+ gc.getName()); 
        	 
        	 for (String s : gc.getMemoryPoolNames())  System.out.println(s);
        	 
        	 
         }
		
	
	}

}

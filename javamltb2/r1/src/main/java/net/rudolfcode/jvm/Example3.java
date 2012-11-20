package net.rudolfcode.jvm;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

/**
 * Is a vector really slower than an arraylist? YES.
 * How much more precise is CPU time measurement than time measurement? ~ ONE ORDER OF MAGNITUDE.
 */
public class Example3 {

	public static void main(String[] args) throws Exception{
		Multimap<String,Long> classes = ArrayListMultimap.create();
		ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
		for(Collection c : new Collection[]{new Vector(), new ArrayList(), new Vector(), new ArrayList(), new Vector(), new ArrayList()} ){
			{
				Thread t1 = new ProfileThread(c.getClass().newInstance());
				long cpuStart1=ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
				t1.start();
				t1.join();
				long cpu1=(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()-cpuStart1);
				classes.put(c.getClass().getName()+"_cpu_time",cpu1);
			}
			{
				Thread t2 = new ProfileThread(c.getClass().newInstance());
				long cpuStart2=ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
				t2.start();
				t2.join();
				long cpu2=(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()-cpuStart2);
				classes.put(c.getClass().getName()+"_total_time",cpu2);
			}
		}
		System.out.println(classes);
	}
	
	public static class ProfileThread extends Thread{
			
			Collection impl;
			public ProfileThread(Collection c){
				impl=c;
			}
		
			@Override
			public void run() {
				impl.clear();
				for(int x = 10 ; x < 1000000; x++){
					impl.add(x);
				}
			}
	}
}

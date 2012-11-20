package net.rudolfcode.jvm;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class Example3 {

	
	public static void main(String[] args) throws Exception{

		long numThreads=ManagementFactory.getThreadMXBean().getTotalStartedThreadCount();

		Multimap<String,Long> classes = HashMultimap.create();
		for(Collection c : new Collection[]{new Vector(), new ArrayList(), new Vector(), new ArrayList(), new Vector(), new ArrayList()} ){
			Thread t1 = new ProfileThread(c);
			long cpuStart1=ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
			t1.start();
			t1.join();
			long cpu=(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()-cpuStart1);
			classes.put(c.getClass().getName(),cpu);
		}

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

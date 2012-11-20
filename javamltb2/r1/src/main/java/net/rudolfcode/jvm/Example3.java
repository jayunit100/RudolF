package net.rudolfcode.jvm;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

public class Example3 {

	
	public static void main(String[] args) throws Exception{

		long numThreads=ManagementFactory.getThreadMXBean().getTotalStartedThreadCount();

		for(int i = 0 ; i < 1000; i++){
			Thread t1 = new ProfileThread(new Vector());
			long cpuStart1=ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
			t1.start();
			t1.join();
			System.out.print(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()-cpuStart1 + " ");
		
			Thread t2 = new ProfileThread(new ArrayList());
			long cpuStart2=ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
			t2.start();
			t2.join();
			System.out.println(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()-cpuStart2);

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

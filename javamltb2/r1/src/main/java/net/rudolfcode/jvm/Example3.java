package net.rudolfcode.jvm;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

/**
 * Is a vector really slower than an arraylist? YES.
 * How much more precise is CPU time measurement *than time measurement?* ~ ONE ORDER OF MAGNITUDE.
   The OUTPUT of this class is a CSV to Standard out. 
 */
public class Example3 {

	public static void main(String[] args) throws Exception{
			profile((int)Math.pow(10, i));
	}

	private static void profile(int nums) {
		 
		ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
				System.out.println("SIZE OF TEST : " +nums);
				final long cpuStart1=System.currentTimeMillis();
				run(new Vector(),nums);
				final long cpu1=(System.currentTimeMillis()-cpuStart1);
				System.out.println("raw time:"+cpu1 + " milliseconds ");

				final long cpuStart2=ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()/1000000;
				run(new Vector(),nums);
				final long cpu2=ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()/1000000;
				System.out.println("cpu time:"+(cpu2-cpuStart2) +" milliseconds");
				System.out.println();
	}
	
	public static float average(Collection<Long> l){
		long value=0;
		for(Long v : l){
			value+=v;
		}
		return ((float)value)/(float)l.size();
	}
		
	/**
	 * Add "size" elements into a collection .
	 */
	public static void run(Collection impl, int size) {
		impl.clear();
		//add a bunch of stuff to a collection
		for(int x = 10 ; x < size; x++){
			impl.add((short)x);
		}
	}

}

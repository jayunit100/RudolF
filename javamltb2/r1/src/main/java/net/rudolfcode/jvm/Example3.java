package net.rudolfcode.jvm;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Is a vector really slower than an arraylist? YES.
 * How much more precise is CPU time measurement than time measurement? ~ ONE ORDER OF MAGNITUDE.
 */
public class Example3 {

	public static void main(String[] args) throws Exception{
		for(float i = 4.0f ; i < 7.4 ; i+=.3){
			profile((int)Math.pow(10, i));
		}
	}

	private static void profile(int nums) {
		Multimap<String,Long> classes = ArrayListMultimap.create();
		ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
		for(Collection c : new Collection[]{new Vector(), new ArrayList()}){
			{
				long cpuStart1=ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
				run(c,nums);
				long cpu1=(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()-cpuStart1);
				classes.put(c.getClass().getName()+"_cpu_time",cpu1);
			}
			{
				long cpuStart2=System.currentTimeMillis();
				run(c,nums);
				long cpu2=(System.currentTimeMillis()-cpuStart2);
				classes.put(c.getClass().getName()+"_total_time",cpu2);
			}
		}
		
		//the "cpu" time : pure measurement of time the cpu is spent during execution.
		Collection<Long> listtime=classes.get("java.util.ArrayList_cpu_time");
		Collection<Long> vectime=classes.get("java.util.Vector_cpu_time");
		
		//the "real world" time : susceptible to fluctuations due to memory and system load.
		Collection<Long> listtimes=classes.get("java.util.ArrayList_total_time");
		Collection<Long> vectimes=classes.get("java.util.Vector_total_time");
		System.out.println(nums + "," + average(listtime)+","+average(vectime)+","+average(listtimes)+","+average(vectimes));
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
		for(int x = 10 ; x < size; x++){
			impl.add((short)x);
		}
	}

}

package net.rudolfcode.jvm;

import java.lang.management.ManagementFactory;

public class Example3A {
	static final double nanose = Math.pow(10, -9);
	public static void main(String[] args){
		ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
		double cputimeSTARTNANO=ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
		double rawtimeSTARTNANO=System.nanoTime();
		double newcputimeSTARTNANO= (ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime());
		double newrawtimeSTARTNANO= System.nanoTime();
		while(1==1){
			try{
				Thread.sleep(1000);
			}
			catch(Exception e){
			}
			newcputimeSTARTNANO= (ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime());
			newrawtimeSTARTNANO= System.nanoTime();
			System.out.println( (newcputimeSTARTNANO-cputimeSTARTNANO)*nanose - (newrawtimeSTARTNANO-rawtimeSTARTNANO)*nanose);//<-- amount of seconds between new and original
			cputimeSTARTNANO=newcputimeSTARTNANO;
			rawtimeSTARTNANO=newrawtimeSTARTNANO;
			
		}
	}
}

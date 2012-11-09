package dev.dump;

import java.io.StringWriter;
import java.util.concurrent.Callable;


/*  This class dumps status on all threads at a specified time in the program 
 *    using  java.util.concurrent.Future
 * 
 *  @ runProcess the name of the process running in the jvm at the time of the dump
 * 	A daemon thread in Java is one that doesn't prevent the JVM from exiting
 * 
 * Methods
 * call:: this interface method aggregates all the system and instance threads to
 * construct a thread dump report 
 * 
 * dumpToDerby:: utility method, serializes data to embedded Derby database
 * 
 * dumpThreadGroupInfo:: aggregates information for child threads of the parent thread groups
 * 
 * this class can be called at any time in the program to get a snap shot of the running processes
 * 
 */

public class Dumper implements Callable<String> {

	final StringWriter sw;
	final String runProcess;
	

	public Dumper(final String runProcess) {
		sw = new StringWriter();
		this.runProcess = runProcess;
		
	}

	@Override
	public String call() {

		// find the top thread group
		ThreadGroup topThreadGroup = Thread.currentThread().getThreadGroup();

		while (topThreadGroup.getParent() != null)
			topThreadGroup = topThreadGroup.getParent();

		sw.append("JVM Thread Info run for process " + runProcess + "\n");

		Thread[] allThreads = new Thread[1000];

		int threadCount = topThreadGroup.enumerate(allThreads, true);

		sw.append("JVM Info Aggregrating Thread Groups All Threads Report\n");

		// dump info o all its threads
		for (Thread th : allThreads) {

			if (th == null)
				continue;

			setReportLine("Top Group Thread" + th.getName(),
					(th.isDaemon() ? "daemon" : "not daemon "),
					(th.isAlive() ? "alive" : "dead "), "to group NA",
					"no stack", runProcess, topThreadGroup.getName());
		}
		
		
		// find all the child gtoups
		ThreadGroup[] allGroups = new ThreadGroup[1000];

		int numThreadGroups = topThreadGroup.enumerate(allGroups, true);

		sw.append("JVM Info Number Thread Groups =" + numThreadGroups + "\n");

		if (numThreadGroups > 1) {

			for (ThreadGroup tg : allGroups) {
				if (tg == null)
					continue;

				// dump info on all its threads
				dumpThreadGroupInfo(tg);
				
				}
			}
		
		return sw.toString();

		
	}
	
	
	void setReportLine(String name, String daemon, String alive, String destroyed, String Stack, String process, String group) {
		
		sw.append(name+"\n");
		sw.append(daemon+"\n");
		sw.append(alive+"\n");
		sw.append(destroyed+"\n");
		sw.append(Stack+"\n");
		sw.append(process+"\n");
	}

	
	// method to report info on child group threads
	public void dumpThreadGroupInfo(ThreadGroup tg) {

		String parentName = (tg.getParent() == null ? "No Parent" : tg.getName());

		// Dump thread group info.

		Thread[] allThreads = new Thread[1000];

		int threadCount = tg.enumerate(allThreads, false);

		for (Thread th : allThreads) {
			if (th == null)
				continue;
			setReportLine("Child Group Thread" + th.getName(),
					(th.isDaemon() ? "daemon" : "not daemon "),
					(th.isAlive() ? "alive" : "dead "), "to group NA",
					"no stack", runProcess, tg.getName());
		}
	}
}

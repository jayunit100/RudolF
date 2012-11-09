package dev.dump;

import java.io.StringWriter;
import java.util.concurrent.Callable;


/*  This class dumps status on all threads at the time specified time it is called
 *  using  java.util.concurrent.Future
 * 
 *  @ runProcess the name of the process running in the jvm at the time of the dump
 * 	A daemon thread in Java is one that doesn't prevent the JVM from exiting
 * 
 * @ call() method of the  Callable in this class is implemented to aggregate
 *   all the system and instance threads to construct a thread dump report 
 * 
 * @ setReportLine() utility method, writes a line to the String output
 * 
 * dumpThreadGroupInfo() aggregates information for child threads of the parent thread groups
 * 
 * this class can be called at any time in the program to get a snap shot of the running threads
 * 
 */

public class Dumper implements Callable<String> {

	final StringWriter sw;
	final String runProcess;
	

	public Dumper(final String runProcess) {
		sw = new StringWriter();
		this.runProcess = runProcess;
		
	}
	
	// java.util.concurrent.Callable. Callables unlike Runnables allow for return values.
	// here we return a String which is a report of the data methods  called on all threads running 
	// when this class is called 
	@Override
	public String call() {

		// find the top thread group
		ThreadGroup topThreadGroup = Thread.currentThread().getThreadGroup();

		// climb up the thread hierarchy tree to the top, this is the parent thread group
		while (topThreadGroup.getParent() != null)
			topThreadGroup = topThreadGroup.getParent();
		
		// initial line for output identify the process this instance created for
		sw.append("JVM Thread Info run for process " + runProcess + "\n");

		// container for threads
		Thread[] allThreads = new Thread[1000];
		
		// count the threads in the parent group
		int threadCount = topThreadGroup.enumerate(allThreads, true);
		
		// initial line for output
		sw.append("JVM Info:: Aggregrating Thread Group Data\n");

		// dump info on all its threads
		for (Thread th : allThreads) {

			if (th == null)
				continue;
			// write data to a line in the report
			setReportLine("Top Group Thread" + th.getName(),
					(th.isDaemon() ? "daemon" : "not daemon "),
					(th.isAlive() ? "alive" : "dead "), "to group NA",
					"no stack", runProcess, topThreadGroup.getName());
		}
		
		
		// container for  all the child groups
		ThreadGroup[] allGroups = new ThreadGroup[1000];

		// count the threads in the the child groups
		int numThreadGroups = topThreadGroup.enumerate(allGroups, true);

		sw.append("JVM Info Number Child Thread Groups =" + numThreadGroups + "\n");

		// if numThreadGroups == 1 them parent so we have data already
		//if numThreadGroups > 1 child group so process for thread data
		if (numThreadGroups > 1) {

			for (ThreadGroup tg : allGroups) {
				// sometimes find null threads so deal with this so loop does not crash with null pointer exception					
				if (tg == null)	continue;

				// dump info on all its threads in this group
				dumpThreadGroupInfo(tg);
				
				}
			}
		
		// return thread data report
		return sw.toString();
		
	}
	
	// a utility method to write a line in the output report String
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
		// get the parent group name
		String parentName = (tg.getParent() == null ? "No Parent" : tg.getName());
		sw.append(parentName+"\n");
		// container for  all the threads
		Thread[] allThreads = new Thread[1000];
		// count the threads
		int threadCount = tg.enumerate(allThreads, false);
		sw.append(parentName+" has "+threadCount+" threads"+"\n");
		
		// get the data for all the threads as a line in the report String
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

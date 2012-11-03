package dev.dump;

import java.io.StringWriter;
import java.util.concurrent.Callable;

public class Dumper implements Callable<String> {
	
	final StringWriter sw;
	final String runProcess;
	
	public Dumper(final String runProcess) {
		sw = new StringWriter();
		this.runProcess = runProcess;
	}
	
	
	@Override
	public String call() {
		
		ThreadGroup topThreadGroup = Thread.currentThread().getThreadGroup();
		
		while(topThreadGroup.getParent()!=null) topThreadGroup=topThreadGroup.getParent();
		
		sw.append("JVM Info for process "+runProcess+"\n");
		
		sw.append("JVM Info Top Thread Group: "+topThreadGroup.getName()+"\n");
		
		Thread[] allThreads = new Thread[1000];
		
		int threadCount = topThreadGroup.enumerate(allThreads,true);
		
		sw.append("JVM Info Top Thread Group Size = "+threadCount+"\n");
		
		sw.append("JVM Info Top Thread Group All Threads Report \n");
		
		for( Thread th :allThreads) {
			
			if(th==null) continue;

			sw.append("TOPGROUP Thread NAME: "+th.getName()

							+(th.isDaemon()?" DAEMON":" NOT DAEMON ")

							+(th.isAlive()?" ALIVE":" DEAD ")+"\n");

				}
		
		
			ThreadGroup[] allGroups = new ThreadGroup[1000];
		
			int numThreadGroups = topThreadGroup.enumerate(allGroups,true);
		
			sw.append("JVM Info Number Thread Groups ="+numThreadGroups+"\n");
	
			if (numThreadGroups >1) {
		
				for (ThreadGroup tg: allGroups ) {
					if(tg==null) continue;
					dumpThreadGroupInfo(tg);
				
				}
		}
		

		    return sw.toString();
	    
		    
	}
	
	
	public void dumpThreadGroupInfo(ThreadGroup tg){  
		
		
		String parentName = (tg.getParent() == null ? "NO PARENT" :tg.getName());
		
		//Dump thread group info.
		sw.append("Child Thread Group:  "+tg.getName()+"\n");

		sw.append("Parent:"+parentName

				+(tg.isDaemon()?"DAEMON":"NOT DAEMON ")

				+(tg.isDestroyed()?"DESTROYED":"NOT DESTROYED")+"\n");

		
			

		Thread[] allThreads = new Thread[1000];
	
		int threadCount= tg.enumerate(allThreads,false);
		
		for( Thread th :allThreads) {
			if(th==null)continue;
			sw.append( parentName+" Thread:"+th.getName()+"\n");

			sw.append((th.isDaemon()?"DAEMON":"NOT DAEMON ")

					+(th.isAlive()?"ALIVE":"DEAD ")+"\n");

				}

			}
		}
	

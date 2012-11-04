package dev.dump;

import gen.ProtoThreadSerDump;
import gen.ProtoThreadSerDump.ThreadSerDump;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.concurrent.Callable;

import dev.derbyutils.DerbyUtils;


/*  This class dumps status on all threads at a specified time in the program 
 *    using  java.util.concurrent.Future
 * 
 *  @ runProcess the name of the process running in the jvm at the time of the dump
 * 	A daemon thread in Java is one that doesn't prevent the JVM from exiting
 */

public class Dumper implements Callable<String> {
	
	final StringWriter sw;
	final String runProcess;
	final int id;
	
	public Dumper(final String runProcess, final int id) {
		sw = new StringWriter();
		this.runProcess = runProcess;
		this.id = id;
	}
	
	
	@Override
	public String call() {
		
		// find the top thread group
		ThreadGroup topThreadGroup = Thread.currentThread().getThreadGroup();
		
		while(topThreadGroup.getParent()!=null) topThreadGroup=topThreadGroup.getParent();
		
		sw.append("JVM Thread Info run for process "+runProcess+"\n");
		
		Thread[] allThreads = new Thread[1000];
		
		int threadCount = topThreadGroup.enumerate(allThreads,true);
		
		sw.append("JVM Info Aggregrating Thread Groups All Threads Report serialize(protoBuff) to Derby \n");
		
		// dump info o all its threads
		for( Thread th :allThreads) {
			
			if(th==null) continue;
							
			dumpToDerby("Top Group Thread"+th.getName(),(th.isDaemon()?"daemon":"not daemon "),
					(th.isAlive()?"alive":"dead "), "to group NA", "no stack", runProcess, id, topThreadGroup.getName());

				}
		
		// find all the child gtoups
			ThreadGroup[] allGroups = new ThreadGroup[1000];
		
			int numThreadGroups = topThreadGroup.enumerate(allGroups,true);
		
			sw.append("JVM Info Number Thread Groups ="+numThreadGroups+"\n");
	
			if (numThreadGroups >1) {
		
				for (ThreadGroup tg: allGroups ) {
					if(tg==null) continue;
					
					// dump info on all its threads
					dumpThreadGroupInfo(tg);
					try {// derby needs time to update
						Thread.sleep(800);
					} catch (InterruptedException e) {
					}
				}
		}
		

		    return sw.toString();
	    
		    
	}
	
	private void dumpToDerby(String name, String daemon, String alive, String destroyed, String Stack, 
																	String process, int id2, String group) {
		
		int uniqueID = id2*100 +id2;
		
		ThreadSerDump serDump = ProtoThreadSerDump.ThreadSerDump.newBuilder() 
				  .setId(uniqueID)
				  .setAlive(alive)
				  .setDaemon(daemon)
				  .setName(name)
				  .setGroup(group)          
				  .setProces(process)
				  .setStackdump(Stack)
				  .build();
				   byte[] store = serDump.toByteArray();
				   int fileLength =  store.length;
				   DerbyUtils.setDumpData(uniqueID, fileLength, store);
	
	}


	// method to report info on child group threads
	public void dumpThreadGroupInfo(ThreadGroup tg){  
		
		
		String parentName = (tg.getParent() == null ? "No Parent" :tg.getName());
		
		//Dump thread group info.

		Thread[] allThreads = new Thread[1000];
	
		int threadCount= tg.enumerate(allThreads,false);
		
		for( Thread th :allThreads) {
			if(th==null)continue;
				
		dumpToDerby("Child Group Thread"+th.getName(),(th.isDaemon()?"daemon":"not daemon "),
				(th.isAlive()?"alive":"dead "), "to group NA", "no stack", runProcess, id, tg.getName());
				}	
			}
		}
	

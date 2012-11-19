package dev;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.SoftReferenceObjectPool;

/** This class measures the impact on System parameters of running the
 *  Garbage collector
 * 
 * we can use the interfaces  org.apache.commons.pool.BasePoolableObjectFactory
 * and  org.apache.commons.pool.PoolableObjectFactory  to create pools of object 
 * then run the System.gc()
 * 
 * Look at what happens for the references in different contexts,  for example 
 *  static final  and static volatile  nested fields.
 *   
 * We could extend this to concurrent pools, we could  look at cost of blocking pools
 * 
 * This implementation would be like an ObjectPool  LinkedBlockingQueue 
 * 
 * We can look at questions like the cost of locks on Objects
 * 
 * there is a good description of how you could implement this here
 * http://javawithswaranga.blogspot.mx/2011/10/generic-and-concurrent-object-pool.html
 * 
 * but for now I will just implement simplest idea with the above apache interfaces
 * 
 *
 * 
 *
 */


public class HeapProfiler {
	
	private static SoftReferenceObjectPool<String[]> pool;
	private static long freeMemory;
	private static  final String nestedLeak = "leak";
	private static volatile String volatileLeak = "leak";

	public static void main(String[] args) throws Exception {
			// get the runtime
		 	final Runtime runtime = Runtime.getRuntime();		
			// SoftReferenceObjectPool GC will reclaim these objects only when the space is really needed	
		   pool = new SoftReferenceObjectPool<String[]>(new SmallObjectFactory());
		   
		   // now perform some simple tests for some changing sets of objects, looking
		   // at memory implications for static final and static volatile
		   	  
		   for (int i = 0 ; i < 1000 ; i++)  pool.addObject();   // put 100 objects in pool
	  
		   freeMemory = runtime.freeMemory();
		   System.out.println("free memory no System.gc "+freeMemory);
		   System.gc();
		   freeMemory = runtime.freeMemory();
		   System.out.println("free memory with System.gc "+freeMemory);		   
		   // clear the pool
		   pool.clear();
		   pool = null;	
		   System.gc();
		   System.gc();
		   pool = new SoftReferenceObjectPool<String[]>(new LeakyObjectFactory());
		   		  
		   for (int i = 0 ; i < 1000 ; i++) pool.addObject();	    // put 100 leaky objects in pool

		   freeMemory = runtime.freeMemory();
		   System.out.println("nested free memory no System.gc "+freeMemory);
		   System.gc();
		   freeMemory = runtime.freeMemory();
		   System.out.println("nested free memory with System.gc "+freeMemory);	   
		   // clear the pool
		   pool.clear();  		   
		   pool = null;	
		   
		   System.gc();
		   System.gc();
		   pool = new SoftReferenceObjectPool<String[]>(new VolatileFieldObjectFactory());
		   		  
		   for (int i = 0 ; i < 1000 ; i++) pool.addObject();	    // put 100 leaky objects in pool

		   freeMemory = runtime.freeMemory();
		   System.out.println("volatile free memory no System.gc "+freeMemory);
		   System.gc();
		   freeMemory = runtime.freeMemory();
		   System.out.println("volatile free memory with System.gc "+freeMemory);	   
		   // clear the pool
		   pool.clear();  		   
		   pool = null;
		   
		   
	}
	
	// make a factory for small pool objects
	  static class SmallObjectFactory implements PoolableObjectFactory<String[]> {

			@Override
			public String[] makeObject() throws Exception {
				String[] obj = new String[1];			
				obj[0] = "leak";
				return obj;
				}		  
			
			// interface methods
			@Override
			public void activateObject(String[] arg0) throws Exception {}
		
			@Override
			public void destroyObject(String[] arg0) throws Exception {}

			@Override
			public void passivateObject(String[] arg0) throws Exception {}
		
			@Override
			public boolean validateObject(String[] arg0) {
				return false;
			}	
	  
	  }
	  
	// make a factory for small pool objects with a static reference leak
	  static class LeakyObjectFactory implements PoolableObjectFactory<String[]> {
			   
			@Override
			public String[] makeObject() throws Exception {
				 String[] obj = new String[1];	 
				 obj[0] = nestedLeak;
				 return obj;
			}
			// interface methods
		@Override
		public void activateObject(String[] arg0) throws Exception {	}
		@Override
		public void destroyObject(String[] arg0) throws Exception {					}

		@Override
		public void passivateObject(String[] arg0) throws Exception {}

		@Override
		public boolean validateObject(String[] arg0) {
			return false;
		}
		          }
		// make a factory for small pool objects with a static volatile reference leak
	  static class VolatileFieldObjectFactory implements PoolableObjectFactory<String[]> {
		   
			@Override
			public String[] makeObject() throws Exception {
				 String[] obj = new String[1];	 
				 obj[0] = volatileLeak;
				 return obj;
			}
			// interface methods
		@Override
		public void activateObject(String[] arg0) throws Exception {	}
		@Override
		public void destroyObject(String[] arg0) throws Exception {					}

		@Override
		public void passivateObject(String[] arg0) throws Exception {}

		@Override
		public boolean validateObject(String[] arg0) {
			return false;
		}
		          }
	  
	  
}

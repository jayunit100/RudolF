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
 * Look at what happens for the references in different contexts, static , final
 * and nested.
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
	private static final  String nestedLeak = "leak";

	public static void main(String[] args) throws Exception {
			// get the runtime
		 	final Runtime runtime = Runtime.getRuntime();		
			// SoftReferenceObjectPool allows garbage collection of unused objects.		
		   pool = new SoftReferenceObjectPool<String[]>(new SmallObjectFactory());
		   	  
		   for (int i = 0 ; i < 1000 ; i++)  pool.addObject();   // put 100 objects in pool
	  
		   freeMemory = runtime.freeMemory();
		   System.out.println(" free memory no System.gc "+freeMemory);
		   System.gc();
		   freeMemory = runtime.freeMemory();
		   System.out.println(" free memory with System.gc "+freeMemory);		   
		   // clear the pool
		   pool.clear();
		   pool = null;	
		   System.gc();
		   System.gc();
		   pool = new SoftReferenceObjectPool<String[]>(new LeakyObjectFactory());
		   		  
		   for (int i = 0 ; i < 1000 ; i++) pool.addObject();	    // put 100 leaky objects in pool

		   freeMemory = runtime.freeMemory();
		   System.out.println(" nested free memory no System.gc "+freeMemory);
		   System.gc();
		   freeMemory = runtime.freeMemory();
		   System.out.println(" nested free memory with System.gc "+freeMemory);	   
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
			  
			  
			@Override
			public void activateObject(String[] arg0) throws Exception {
				// TODO Auto-generated method stub
				
			}
		
			@Override
			public void destroyObject(String[] arg0) throws Exception {
				// TODO Auto-generated method stub
				
			}
		
		
		
			@Override
			public void passivateObject(String[] arg0) throws Exception {
				// TODO Auto-generated method stub
				
			}
		
			@Override
			public boolean validateObject(String[] arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			  
			  
		
	  
	  }

	  static class LeakyObjectFactory implements PoolableObjectFactory<String[]> {
			   
			@Override
			public String[] makeObject() throws Exception {
				 String[] obj = new String[1];	 
				obj[0] = nestedLeak;
				return obj;
			}


		@Override
		public void activateObject(String[] arg0) throws Exception {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void destroyObject(String[] arg0) throws Exception {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void passivateObject(String[] arg0) throws Exception {
			// TODO Auto-generated method stub
			
		}


		@Override
		public boolean validateObject(String[] arg0) {
			// TODO Auto-generated method stub
			return false;
		}
		          }
	  
	  
	  
	  
	  
}

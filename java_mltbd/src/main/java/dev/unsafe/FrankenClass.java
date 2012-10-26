package dev.unsafe;




import java.lang.reflect.Field;
import sun.misc.Unsafe;


/* wrapper for the unsafe class */

public class FrankenClass {
	// object to investigate or manipulate
	private static Object testObj;
	
	public FrankenClass(Object o) {
		this.testObj = o;
	
	}
		
	// initial utility method to return size of object, more to come
	public long sizeOf() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Unsafe unsafe = getUnsafeInstance();
	
		   return unsafe.getAddress( normalize( unsafe.getInt(testObj, 4L) ) + 12L );
		}
	
	// pseudo factory method to obtain unsafe instance
	 private static Unsafe getUnsafeInstance()throws SecurityException, NoSuchFieldException, IllegalArgumentException,
	   IllegalAccessException {
	  Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
	  theUnsafeInstance.setAccessible(true);
	  return (Unsafe) theUnsafeInstance.get(Unsafe.class);
	 }

	
	
	// gives absolute value    
	public  long normalize(int value) {
		   if(value >= 0) return value;
		   return (~0L >>> 32) & value;
		}
	    
	}
	


package dev.unsafe;




import java.lang.reflect.Field;
import sun.misc.Unsafe;


/* wrapper for the unsafe class */

public class FrankenClass {
	// object to investigate or manipulate
	private static Object testObj;
	
	
	public FrankenClass() {
		
	
	}
	
	
	public FrankenClass(Object o) {
		this.testObj = o;
	
	}
		
	
	// initial utility method to return size of object, more to come
	// C-like sizeof() function, returns shallow object size in bytes
	public long sizeOf() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Unsafe unsafe = getUnsafeInstance();
		
	//getInt(testObj, 4L) --> 4L  gets the class address for the testObj, from its look up table, 4L means go up 4 bytes in table
	// unsafe.getAddress(x + 12L )	gets the class capture_layout_helper field which is instance size in bytes
	//	normalize() gets absolute value
	// This function will not work for array objects _layout_helper field has another meaning in array context	
		
		   return unsafe.getAddress( normalize( unsafe.getInt(testObj, 4L) ) + 12L );
		}
	
	
	
	public long sizeOf(Object o) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Unsafe unsafe = getUnsafeInstance();
		
	//getInt(testObj, 4L) --> 4L  gets the class address for the testObj, from its look up table, 4L means go up 4 bytes in table
	// unsafe.getAddress(x + 12L )	gets the class capture_layout_helper field which is instance size in bytes
	//	normalize() gets absolute value
	// This function will not work for array objects _layout_helper field has another meaning in array context	
		
		   return unsafe.getAddress( normalize( unsafe.getInt(o, 4L) ) + 12L );
		}
	

	
	// pseudo factory method to obtain unsafe instance
	public Unsafe getUnsafeInstance()throws SecurityException, NoSuchFieldException, IllegalArgumentException,
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
	


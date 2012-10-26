package dev.unsafe;




import java.lang.reflect.Field;
import sun.misc.Unsafe;


/* wrapper for the unsafe class now works */

public class FrankenClass {
	
	private static Object testObj;
	
	public FrankenClass(Object o) {
		this.testObj = o;
	//	System.setProperty("java.security.policy", PolicyFileLocator.getLocationOfPolicyFile());
	}
		
	 private static Unsafe getUnsafeInstance()throws SecurityException, NoSuchFieldException, IllegalArgumentException,
	   IllegalAccessException {
	  Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
	  theUnsafeInstance.setAccessible(true);
	  return (Unsafe) theUnsafeInstance.get(Unsafe.class);
	 }

	public long sizeOf() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Unsafe unsafe = getUnsafeInstance();
	//	 return 12;
		   return unsafe.getAddress( normalize( unsafe.getInt(testObj, 4L) ) + 12L );
		}
	    
	public  long normalize(int value) {
		   if(value >= 0) return value;
		   return (~0L >>> 32) & value;
		}
	    
	}
	


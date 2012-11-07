package dev.unsafe;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * 
 *  NOTE WE SHOULDNT USE THIS IN UNIT TESTS BECAUSE IT CAN BREAK THE BUILD
 * This class demonstrates a technique to obtain via reflection an instance of
 * sun.misc.unsafe

 *  Methods
 *  
 *  getUnsafeInstance::  utility method to obtain via reflectioan instance of sun.misc.unsafe
 *  
 *  this class demonstrate use of the markers in the underlying C structs for java Class and objects
 *  to perform direct memory operations
 *  
 *  inspect the structs in the code for more info on java objects
 *  http://hg.openjdk.java.net/jdk7/hotspot/hotspot/file/9b0ca45cd756/src/share/vm/oops/oop.hpp
 *  inspect the structs in the code for more info on java class
 *  http://hg.openjdk.java.net/jdk7/hotspot/hotspot/file/9b0ca45cd756/src/share/vm/oops/klass.hpp
 *  
 *  this class demonstrate the underlying address and memory structure architecture of the JVM
 *  
 */
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
	public long sizeOf() throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Unsafe unsafe = getUnsafeInstance();

		// getInt(testObj, 4L) --> 4L gets the class address for the testObj,
		// from its look up table, 4L means go up 4 bytes in table
		// unsafe.getAddress(x + 12L ) gets the class capture_layout_helper
		// field which is instance size in bytes
		// normalize() gets absolute value
		// This function will not work for array objects _layout_helper field
		// has another meaning in array context

		return unsafe.getAddress(normalize(unsafe.getInt(testObj, 4L)) + 12L);
	}

	public long sizeOf(Object o) throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Unsafe unsafe = getUnsafeInstance();

		// getInt(testObj, 4L) --> 4L gets the class address for the testObj,
		// from its look up table, 4L means go up 4 bytes in table
		// unsafe.getAddress(x + 12L ) gets the class capture_layout_helper
		// field which is instance size in bytes
		// normalize() gets absolute value
		// This function will not work for array objects _layout_helper field
		// has another meaning in array context

		return unsafe.getAddress(normalize(unsafe.getInt(o, 4L)) + 12L);
	}

	// pseudo factory method to obtain unsafe instance
	public Unsafe getUnsafeInstance() throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
		theUnsafeInstance.setAccessible(true);
		return (Unsafe) theUnsafeInstance.get(Unsafe.class);
	}

	// gives absolute value
	public long normalize(int value) {
		if (value >= 0)
			return value;
		return (~0L >>> 32) & value;
	}

}

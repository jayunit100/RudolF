package dev.dirmemaccess;

import java.util.HashMap;
import java.util.List;

import dev.testobjects.StructureWrapper;
import dev.testobjects.StructureWrapper.MyStructureOneInt;
import dev.unsafe.FrankenClass;


/*  This class demonstrates a technique to copy 
 * 	an object to an off heap memory address
 * 
 *  @ structure test object for memory tests by sun.misc.Unsafe.class
 * 
 *  @ FrankenClass is a wrapper class to obtain an instance of sun.misc.Unsafe.class 
 *  @ Pointer p is just a memory address for am object on the heap
 *  @ offheapPointeris is just a memory address for am object off the heap 
 *  
 *  Methods
 *  
 *  printAddressAllocation::  utility method stores address locations in a hash map so can be displayed
 *  copyMemory::  use sun.misc.unsafe to directly copy memory from java heap to native buffers
 *  getOffHeapAdressMemory::  uses reflection and sun.misc.unsafe to obtain memory address of 
 *  object copied to native buffers
 *  
 *  this class demonstrate use of the markers in the underlying C structs for java Class and objects
 *  to perform direct memory operations
 *  
 * 
 */



public class DirectMemAccess {

	private static final MyStructureOneInt structure;
	private static final FrankenClass frankenObj;
	private Pointer p;
	private long size;
	private long offheapPointer;

	// static initialiser
	static {

		structure = new StructureWrapper().new MyStructureOneInt();
		frankenObj = new FrankenClass();
	}

	@SuppressWarnings("restriction")
	public DirectMemAccess(int value) {
		// give the test object a value for testing
		structure.x = value;
		try {
			size = frankenObj.sizeOf(structure);

			// allocate memory is off the heap in the sense that it is not
			// available to the garbage collector
			// and not limited to JVM heap sizs restrictions
			offheapPointer = frankenObj.getUnsafeInstance()
					.allocateMemory(size);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	// just prints memory address
	public HashMap<String, Long> printAddressAllocation() {
		HashMap<String, Long> malloc = new HashMap<String, Long>();
		malloc.put("size", size);
		malloc.put("offheapPointer", offheapPointer);
		return malloc;
	}

	@SuppressWarnings("restriction")
	public void copyMemory() {
		// copy memory contents from one address to another
		try {
			frankenObj.getUnsafeInstance().copyMemory(structure, // source
																	// object
					0, // source offset is zero - copy an entire object
					null, // destination is specified by absolute address, so
							// destination object is null
					offheapPointer, // destination address
					size);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		System.out.println(" test object was copied to off-heap");

	}

	// set pointer to off-heap copy of the test object
	@SuppressWarnings("restriction")
	public void getOffHeapAdressMemory() {
		p = new Pointer();
		try {
			long pointerOffset = frankenObj.getUnsafeInstance()
					.objectFieldOffset(
							Pointer.class.getDeclaredField("pointer"));

			// set pointer to off-heap copy of the test object
			// putLong is directly accessing the underlying c struct that
			// represents a java object
			frankenObj.getUnsafeInstance().putLong(p, pointerOffset,
					offheapPointer);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		System.out.println(" pointer to off heap memory now set");

	}
	
	// get set methods

	public void setNewHeapMemoryValue(int x) {

		structure.x = x;
		System.out.println("structure.x now has heap value: " + structure.x);
	}

	public void getOffHeapMemoryValue() {
		// here are pointer is an address to the off heap memeory
		int y = ((MyStructureOneInt) p.pointer).x;

		System.out.println("structure.x has off heap value: " + y);
	}

	public void getCurrentObjHeapValue() {

		System.out
				.println("structure.x has current heap value: " + structure.x);
	}

	class Pointer {
		Object pointer;
	}

}

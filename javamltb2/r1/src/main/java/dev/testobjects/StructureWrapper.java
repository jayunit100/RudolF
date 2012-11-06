package dev.testobjects;

public class StructureWrapper {
	
	/*  This class is a container for test objects 

	 *  @ structurels are test objects for memory tests by sun.misc.Unsafe.class
	 *  
	 */
	
	
	public class MyStructureEmpty { } // 8: 4 (start marker) + 4 (pointer to class)
	public class MyStructureOneInt { public int x; } // 16: 4 (start marker) + 4 (pointer to class) + 4 (int) + 4 stuff bytes to align structure to 64-bit blocks
	public class MyStructureTwoInt { int x; int y; } // 16: 4 (start marker) + 4 (pointer to class) + 2*4
	
	

}

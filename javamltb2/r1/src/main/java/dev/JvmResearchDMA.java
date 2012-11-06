package dev;

import java.util.HashMap;

import dev.dirmemaccess.DirectMemAccess;
import dev.dirmemaccess.HostileJavaString;

/* 
 * This class is the entry point for JVM structural models and 
 * memory address scheme investigation.
 * 
 * Currently this class only runs in java 7
 * 
 * It demonstrates how you can use perform direct memory copy
 * and write java objects to off heap native buffers.
 * 
 * It demonstrates how you can bypass the standard java 
 * security model using reflection to perform illegal code operations
 * 
 * this class demonstrates how easy it is to write a worm that will steal your system memory 
 * 
 * */

public class JvmResearchDMA {

	static DirectMemAccess worm;

	public static void main(String[] args) throws Exception {

		// start code to  demonstrates direct memory access and off heap object
		// allocation
		worm = new DirectMemAccess(777);
		System.out.println("process off heap memory accress code");
		HashMap<String, Long> map = worm.printAddressAllocation();
		System.out
				.println("mem size of structure in bytes: " + map.get("size"));
		System.out.println("address of structure : long: "
				+ map.get("offheapPointer"));
		// this class is a worm because a hostile object with DMA could steal
		// all your system memory by crating and mapping objects from the heap to your system
		// also could do the same thing with your disk with file writes

		worm.copyMemory();
		worm.getOffHeapAdressMemory();
		worm.getCurrentObjHeapValue();
		worm.setNewHeapMemoryValue(222);
		worm.getOffHeapMemoryValue();
		
		// end  code to  demonstrates direct memory access and off heap object
		
		// start code 	to perform illegal object cast from type string to type worm
		System.out.println("end process off heap memory accress code" + "\n");
		System.out.println("process hostile java string code" + "\n");
		// create mutated string wrapper object
		HostileJavaString virus = new HostileJavaString();
		System.out.println("this is host string:  " + virus.getHostCarrier());
		System.out.println("make hostile java string");
		virus.infectHost();
		System.out.println("Show embedded object in infected String");
		virus.exposePathogen();
		// end  code to  demonstrates direct memory access and off heap object
	}

}

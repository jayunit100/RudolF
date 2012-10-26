package dev;

import java.util.HashMap;

import dev.dirmemaccess.DirectMemAccess;



/* this class opens up a can of worms */

public class JvmResearchDMA {
	
	static DirectMemAccess worm;	
	
	public static void main(String[] args) throws Exception{	
		
		worm = new DirectMemAccess(777);
		
		 HashMap<String, Long > map =  worm. printAddressAllocation();
		 System.out.println("mem size in bytes: "+map.get("size"));
		 System.out.println("address: long: "+map.get("offheapPointer"));		 
		 
		 worm.copyMemory();
		 worm.getOffHeapAdressMemory();		
		 worm.getCurrentObjHeapValue();
		 worm.setNewHeapMemoryValue(222);
		 worm.getOffHeapMemoryValue();
		 
		 
		
	}
	
	

}

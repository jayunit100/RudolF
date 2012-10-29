package dev;

import java.util.HashMap;

import dev.dirmemaccess.DirectMemAccess;
import dev.dirmemaccess.HostileJavaString;



/* this class opens up a can of worms */

public class JvmResearchDMA {
	
	static DirectMemAccess worm;	
	
	public static void main(String[] args) throws Exception{
		
		// this code demonstrates direct memory access and off heap object allocation
		 worm = new DirectMemAccess(777);
		 System.out.println("process off heap memory accress code");	
		 HashMap<String, Long > map =  worm. printAddressAllocation();
		 System.out.println("mem size of structure in bytes: "+map.get("size"));
		 System.out.println("address of structure : long: "+map.get("offheapPointer"));		 
		 
		 worm.copyMemory();
		 worm.getOffHeapAdressMemory();		
		 worm.getCurrentObjHeapValue();
		 worm.setNewHeapMemoryValue(222);
		 worm.getOffHeapMemoryValue();
		 
		 System.out.println("end process off heap memory accress code"+"\n");
		 System.out.println("process hostile java string code"+"\n");
		 // create mutated string wrapper object
		 HostileJavaString virus = new HostileJavaString();
		 System.out.println("this is host string:  "+virus.getHostCarrier());
		 System.out.println("make hostile java string");
		 virus.infectHost();
		 System.out.println("Show embedded object in infected String");
		 virus.exposePathogen();
		 
		 
		 
		 
	}
	
	

}

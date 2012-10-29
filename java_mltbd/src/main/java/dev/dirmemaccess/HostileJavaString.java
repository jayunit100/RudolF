package dev.dirmemaccess;
import dev.unsafe.FrankenClass;

public class HostileJavaString {
	
	

	private static final 	FrankenClass frankenObj;
	
	// static initialiser
	static { 	
				frankenObj = new FrankenClass();
			
	}
	

	public void infectHost() {
		// set up patheogenic mutated virus class
		 Worm	worm = new  Worm();
		 // set up pathogen
		 worm.pathogen = "this is a virus";
		 // create carrier
		 String hostCarrier = "kiss me";
		 // use unsafe to infect host
		try {
			long carrierClassAddress = frankenObj.normalize( frankenObj.getUnsafeInstance().getInt(worm, 4L) );
			// insert pointer to String
			long stringClassAddress =frankenObj. normalize( frankenObj.getUnsafeInstance().getInt("", 4L) );				
				frankenObj.getUnsafeInstance().putAddress(carrierClassAddress + 36, stringClassAddress);
			} catch (SecurityException e) {	e.printStackTrace();} catch (NoSuchFieldException e) {
				e.printStackTrace();} catch (IllegalArgumentException e) {e.printStackTrace(); } catch (IllegalAccessException e) {e.printStackTrace();}
		System.out.println("this is the host string");
		System.out.println(hostCarrier);
		hostCarrier = (String)(Object)worm ; // With out direst memory access ClassCastException
		exposePathogen(  hostCarrier);		}
		
	void exposePathogen(String message) {
		System.out.println("this is the  embedded hostile  object in host string");  
		System.out.println( ((Worm)(Object)message).pathogen );	}
	// virus object
	class Worm  {
	   String pathogen; }
	

}

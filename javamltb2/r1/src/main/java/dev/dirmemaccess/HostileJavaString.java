package dev.dirmemaccess;

import dev.unsafe.FrankenClass;

/**  
 *   This class is an example of a worm or virus
 * 	it demonstrates how an object can steal a system super class
 *  type and then perform illegal casts
 *  @ structure test object for memory tests by sun.misc.Unsafe.class
 * 
 *  @ FrankenClass is a wrapper class to obtain an instance of sun.misc.Unsafe.class 
 *  @ hostCarrier is just a string , we will steal its superclass address
 *  @ Worm is just a simple object with a field  that can hold values, it will pretend to be
 *  of type String.
 *  
 *  Methods
 *  
 *  infectHost:: uses sun.misc.unsafe to obtain memory address of a marker in the underlying 
 *  class C struct, this marker is the memory address of the super class, the java.system.String 
 *  superclass is then hijacked by the Wirm class and used to appear to the JVM as String type
 *  
 *  exposePathogen:: shows how the result of the infectHost method can be used to perform an illegal 
 *  cast from type worm to type java.lang.String  "hostCarrier = (String) (Object) worm;"
 *  
 *  this class demonstrates the use of the underlying C struct markers for Java classes and objects, 
 *  and making low level system calls to deceive the JVM
 */
@SuppressWarnings("restriction")
public class HostileJavaString {

	private static final FrankenClass frankenObj;
	private static String hostCarrier = "kiss me";
	private static Worm worm;

	// static initialiser
	static {
		frankenObj = new FrankenClass();

	}

	public void infectHost() {
		// set up patheogenic mutated virus class
		worm = new Worm();
		// set up pathogen
		worm.pathogen = "this is a virus";
		// create carrier

		// use unsafe to infect host
		try {
			// get the Worms classes C struct mem address
			
			long wormClassAddress = frankenObj.normalize(frankenObj
					.getUnsafeInstance().getInt(worm, 4L));
			// insert pointer to String
			long stringClassAddress = frankenObj.normalize(frankenObj
					.getUnsafeInstance().getInt("", 4L));
			// here we go to the worms class's C struct definition then shift 36
			// bytes to change the memory address of the SuperClass
			// and change it to the java.lang.String classes Superclass
			frankenObj.getUnsafeInstance().putAddress(wormClassAddress + 36,
					stringClassAddress);
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

	public void exposePathogen() {
		System.out.println("this is the host string");
		System.out.println(hostCarrier);
		hostCarrier = (String) (Object) worm;

		System.out
				.println("this is the  embedded hostile  object in host string");
		// normally this class would be illegal but we have hijacked the address
		// of the java.lang.String superclass
		// so we can be a String even though we are a worm as well
		System.out.println(((Worm) (Object) hostCarrier).pathogen);
	}

	/**
	 * Virus object
	 * @author jayunit100
	 *
	 */
	class Worm {
		String pathogen;
	}

	public static String getHostCarrier() {
		return hostCarrier;
	}

}

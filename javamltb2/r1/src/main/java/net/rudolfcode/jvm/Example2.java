package net.rudolfcode.jvm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * What happens when the JVM attempts to synchronize() a method call? 
 * How long does this take? How many operations does it involve? 
 * 
 * These are all difficult questions to answer- Nigel how detailed of an answer can you provide?
 * @author jayunit100
 */
public class Example2 {
	
	public static void populate(Collection c){
		for(int i = 0 ; i < SIZE; i++){
			c.add("a string");
		}
	}
	
	static int SIZE=100000;
	public static void main(String[] args){
		for(int i = 0 ; i < 10 ; i++){
			long start=0L;
			
			//Nigel: The vector generally takes 2 to 3 times longer for adding these objects.  Why?
			start=System.currentTimeMillis();
			Vector v = new Vector(SIZE);
			populate(v);
			System.out.println(System.currentTimeMillis()-start);
	
			start=System.currentTimeMillis();
			List l = new ArrayList(SIZE);
			populate(l);
			System.out.println(System.currentTimeMillis()-start);
			System.out.println("--");
		}
	}
}

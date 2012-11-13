package net.rudolfcode.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * In this very simple, single threaded example, we will monitor the increasing 
 * memory load taken up by a simple loop which creates a list of lists, where each list has a string in it.  
 * 
 * The data structure created in main() memory is like this: 
 * [ ["A string"] ["A string"] ["A string"] ...] 
 * 
 * However, interestingly, even though the "A string" is exactly 8 bytes, each memory iteration increase the
 * heap size by 20 bytes.  We can thus learn about the JVM here, in particular: 
 * 
 * How much memory does an ArrayList take up, and why?
 * 
 * @author jayunit100
 */
public class Example1 {

	public static void main(String[] args){
		List myListHolder = new ArrayList();
		for(int i = 0 ; i < 10000; i++){
			List<String> l = new ArrayList<String>();
			l.add("A string");
			myListHolder.add(l);
		//	System.gc(); //<-- Auxillary question: Nigel - why is this call necessary to get a clear value for "freeMemory"?
			System.out.println(i+": "+Runtime.getRuntime().freeMemory());
		}
	}
}

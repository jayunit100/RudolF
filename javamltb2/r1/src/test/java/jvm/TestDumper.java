package jvm;

import org.junit.Test;

import dev.JVMProfiler;
import dev.JvmResearchTest;

public class TestDumper {

	@Test
	public void test1()  throws Exception {
		
		JvmResearchTest.stageOneTestNewCode();
	}
	
	
	@Test
	public void test2()  throws Exception {
		
		JvmResearchTest.stageTwoTestNewCode(); 
		

	}
	

	@Test
	public void test3()  throws Exception {
		
		JvmResearchTest.stageThreeTestNewCode(); 
		

	}
	
	
	
	
}

package net.rudolfcode.jvm;

/**
 * How many instructions can the JVM execute in a second?
 * This code demonstrates that we can increment a variable 
 * 2E8 times in a second, which is close to the theoretical limit 
 * of a modern processor ().
 * 
 * @author jayunit100
 */
public class Example3B {
	public static void main(String[] args){
			Thread addThread = createThread();
			runForASecond(addThread,1000);
	}

	private static Thread createThread() {
		Thread addThread = new Thread(){
			Long i =0L;
			public void run() {
				for (;;) {
					try {
						i++;
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			@Override
			public void interrupt() {
				System.out.println(i);
				super.interrupt();
			}
		};
		return addThread;
	}
	
	private static void runForASecond(Thread addThread, int milli) {
		addThread.start();
		try{
			Thread.sleep(milli);
		}
		catch(Exception e){
			
		}
		addThread.interrupt();
		//stop() works on some JVMs...
		addThread.stop();
	}
}
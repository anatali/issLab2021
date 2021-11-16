package it.unibo.enabler;

public class MultiCalls {
	
 	private TcpEnabler le;
	
	public void setup() throws Exception {
		le    = new LedEnabler(8010, true);
 		le.start();
 		work();
 	}
	
	public void work() throws Exception {
		EnablerClient c1_led   = new EnablerClient( "localhost", le.getPort());
		EnablerClient c2_led   = new EnablerClient( "localhost", le.getPort());
		c1_led.forward("on from c1");
		//Thread.sleep(1000);
		c2_led.forward("off from c2");
		c1_led.forward("off form c1");
	}
	
	public static void main( String[] args) throws Exception {
		new MultiCalls().setup();
		Thread.sleep(2000);
		System.out.println("BYE  ");
	}

}

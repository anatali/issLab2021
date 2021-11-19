package it.unibo.radarWithEnabler;

import it.unibo.enabler.TcpClient;
import it.unibo.enabler.TcpEnabler;

public class MultiCalls {
	
 	private TcpEnabler le;
	
	public void setup() throws Exception {
		new LedEnabler(8010, true);
 		work();
 	}
	
	public void work() throws Exception {
		TcpClient c1_led   = new TcpClient( "localhost", 8010, null);
		TcpClient c2_led   = new TcpClient( "localhost", 8010, null);
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

package it.unibo.enabler;

import java.net.InetAddress;

public class Controller{
	public final int DLIMIT = 30;	
	private EnablerClient c_led,c_radarGui,c_sonar ;
	private final int controllerPort = 8016;
	//private IEnabler radarGui, led, sonar;
 
	
	//TODO: annotation/injection
	public Controller( IEnabler radarGui, IEnabler led, IEnabler sonar ) throws Exception {
		//this.radarGui = radarGui;
		//this.led      = led;
		//this.sonar    = sonar;
		c_led         = new EnablerClient( "192.168.1.4", led.getPort());
		c_radarGui    = new EnablerClient( "localhost",   radarGui.getPort());
		c_sonar       = new EnablerClient( "192.168.1.4", sonar.getPort());	
		
		new ControllerEnabler(controllerPort, this);
		
		System.out.println("IP============="+ InetAddress.getLocalHost().getHostAddress() );
		
		c_sonar.forward("{\"host\":\"192.168.1.22\", \"port\":\"8016\"}");  //Come sa 8016?
	}
/*
	@Override
	public void run() {
		try {
			c_led         = new EnablerClient( "192.168.1.4", led.getPort());
			c_radarGui    = new EnablerClient( "localhost",   radarGui.getPort());
			c_sonar       = new EnablerClient( "192.168.1.4", sonar.getPort());			 
 		} catch (Exception e) {
 			e.printStackTrace();
		}	
	}
*/	
	public void doWork( String distance ) { 
		try {
			/*
			for( int i=1; i<=8; i++) {
				int d =  i*10;
				System.out.println("Controller | simulate sonar distance  " + d);
				//String msg = "sonarDistance("+d+")";
				 * 
			*/
			int d = Integer.parseInt(distance);
				c_radarGui.forward(distance);
				if( d < DLIMIT ) c_led.forward("on");
				else c_led.forward("off");
				//Thread.sleep(1000);
			
		} catch (Exception e) {
 			e.printStackTrace();
		}	
		
	}
	
	

}

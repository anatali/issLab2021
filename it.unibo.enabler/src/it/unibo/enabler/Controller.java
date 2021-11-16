package it.unibo.enabler;

import java.net.DatagramSocket;
import java.net.InetAddress;
 

public class Controller{
	public final int DLIMIT = 15;	
	private EnablerClient c_led,c_radarGui,c_sonar ;
	 
	public Controller(   )  {
		try {
	 		c_led         = new EnablerClient( DeviceEnablerActivator.hostAddr, DeviceEnablerActivator.ledEnablerPort);
			c_sonar       = new EnablerClient( DeviceEnablerActivator.hostAddr, DeviceEnablerActivator.sonarEnablerPort);	
			c_radarGui    = new EnablerClient( "localhost", DeviceEnablerActivator.radarGuiEnablerPort);
			
			new ControllerEnabler(DeviceEnablerActivator.controllerEnablerPort, this);
			
			String myIp = findMyIp();
			String jsonMsg = "{\"host\":\"HOST\", \"port\":\"PORT\"}".
					replace("HOST",myIp).replace("PORT",""+DeviceEnablerActivator.controllerEnablerPort);
			c_sonar.forward(jsonMsg);   	//sonar activation message 
		} catch( Exception e) {
			System.out.println("Controller | ERROR "+ e.getMessage() );
		}
	}
 
	protected String findMyIp() throws Exception {
		DatagramSocket socket = new DatagramSocket();
	    socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
	    String ip = socket.getLocalAddress().getHostAddress();
	    System.out.println("MYIP ============="+ ip );
	    socket.close();
	    return ip;	
	}
	
	//Called by ControllerEnabler
	public void doWork( String distance ) { 
		try {
			int d = Integer.parseInt(distance);
				c_radarGui.forward(distance);
				if( d < DLIMIT ) c_led.forward("on");
				else c_led.forward("off");
		} catch (Exception e) {
 			e.printStackTrace();
		}	
		
	}
	
	

}

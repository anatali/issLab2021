package it.unibo.radarWithEnabler;

import java.net.DatagramSocket;
import java.net.InetAddress;

import it.unibo.enabler.TcpClient;
 

public class Controller{
	public final int DLIMIT = 15;	
	private TcpClient c_led,c_radarGui,c_sonar ;
	 
	public Controller(   )  {
		try {
			/*
			 * Il Controller gestisce i dsipositivi realizzando la business logic
			 */
	 		c_led         = new TcpClient( DeviceEnablerActivator.hostAddr, DeviceEnablerActivator.ledEnablerPort, null);
			c_sonar       = new TcpClient( DeviceEnablerActivator.hostAddr, DeviceEnablerActivator.sonarEnablerPort, null);	
			c_radarGui    = new TcpClient( "localhost", DeviceEnablerActivator.radarGuiEnablerPort, null);
			
			/*
			 * Al Controller del RadarSystem invia messaggi il sonar
			 */
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

package it.unibo.enablerCleanArch.adapters;

import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.enablers.EnablerAsClient;
import it.unibo.enablerCleanArch.enablers.LedEnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Interaction2021;


/*
 * Adapter for the output device  Led
 */
public class LedAdapterEnablerAsClient extends EnablerAsClient implements ILed {
private boolean ledStateMirror = false;

	public LedAdapterEnablerAsClient( String name, String host, int port, ProtocolType protocol  ) {
		super(name,host,port, protocol);
		System.out.println(name+" |  STARTS for " + host +":"+port);
	}

	@Override
	public void turnOn() { 
 		try {
			sendValueOnConnection( "on" );
			ledStateMirror = true;
		} catch (Exception e) {
			System.out.println(name+" |  turnOn ERROR " + e.getMessage() );
		}
 	}

	@Override
	public void turnOff() {   
 		try {
			sendValueOnConnection( "off" );
			ledStateMirror = false;
		} catch (Exception e) {
			System.out.println(name+" |  turnOff ERROR " + e.getMessage() );
		}
 	}

	@Override
	public boolean getState() {   
		return ledStateMirror;
	}
	
	@Override
	protected void handleMessagesFromServer(Interaction2021 conn) throws Exception {
		while( true ) {
			String msg = conn.receiveMsg();  //bòlocking
			System.out.println(name+" |  I should be never here .... " + msg   );		
		}
	}

	
	/*
	 * A rapid test ...
	 */
	public static void main( String[] args) throws Exception {
		RadarSystemConfig.simulation = true;
		RadarSystemConfig.ledPort    = 8015;
 		RadarSystemConfig.testing    = false;
		
		ILed led = DeviceFactory.createLed();
		
		new LedEnablerAsServer("LedEnablerAsServer",RadarSystemConfig.ledPort, ProtocolType.tcp, led );

		ILed ledClient = new LedAdapterEnablerAsClient(
				"LedAdapterEnablerAsClient", "localhost",RadarSystemConfig.ledPort, ProtocolType.tcp );
			
		ledClient.turnOn();
		Thread.sleep(500);
		ledClient.turnOff();
		Thread.sleep(500);
		/*	 */
		System.exit(0);
	}
	
}

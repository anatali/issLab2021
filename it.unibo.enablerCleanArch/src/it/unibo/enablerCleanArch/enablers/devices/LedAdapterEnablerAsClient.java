package it.unibo.enablerCleanArch.enablers.devices;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.enablers.EnablerAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
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
 			sendCommandOnConnection( "on" );
			ledStateMirror = true;
		} catch (Exception e) {
			System.out.println(name+" |  turnOn ERROR " + e.getMessage() );
		}
 	}

	@Override
	public void turnOff() {   
 		try {
 			sendCommandOnConnection( "off" );
			ledStateMirror = false;
		} catch (Exception e) {
			System.out.println(name+" |  turnOff ERROR " + e.getMessage() );
		}
 	}

	@Override
	public boolean getState() {   
		return ledStateMirror;
	}

	/*
	@Override
	protected void handleMessagesFromServer(Interaction2021 conn) throws Exception {
		while( true ) {
			String msg = conn.receiveMsg();  //blocking
			System.out.println(name+" |  answer=" + msg   );		
		}
	}
*/
 
	
}

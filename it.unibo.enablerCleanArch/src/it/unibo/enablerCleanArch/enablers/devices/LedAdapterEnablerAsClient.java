package it.unibo.enablerCleanArch.enablers.devices;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.enablers.EnablerAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.supports.Colors;
 
/*
 * Adapter for the output device  Led
 */
public class LedAdapterEnablerAsClient extends EnablerAsClient implements ILed {

	public LedAdapterEnablerAsClient( String name, String host, int port, ProtocolType protocol  ) {
		super(name,host,port, protocol);
		Colors.out(name+" |  STARTS for " + host +":"+port);
	}

	@Override
	public void turnOn() { 
 		try {
 			sendCommandOnConnection( "on" );
		} catch (Exception e) {
			Colors.outerr(name+" |  turnOn ERROR " + e.getMessage() );
		}
 	}

	@Override
	public void turnOff() {   
 		try {
 			sendCommandOnConnection( "off" );
		} catch (Exception e) {
			Colors.outerr(name+" |  turnOff ERROR " + e.getMessage() );
		}
 	}

	@Override
	public boolean getState() {   
		String answer = sendRequestOnConnection( "getState" );
		//Colors.out(name+" |  getState answer " + answer );
		return answer.equals("true");
	}
}

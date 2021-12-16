package it.unibo.enablerCleanArch.enablers.devices;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.supports.Colors;
 
/*
 * Adapter for the output device  Led
 */
public class LedProxyAsClient extends ProxyAsClient implements ILed {

	public LedProxyAsClient( String name, String host, String entry, ProtocolType protocol  ) {
		super(name,host,entry, protocol);
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

package it.unibo.enablerCleanArch.enablers.devices;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;
 
/*
 * Adapter for the output device  Led
 */
public class LedProxyAsClient extends ProxyAsClient implements ILed {

	public LedProxyAsClient( String name, String host, String entry, ProtocolType protocol  ) {
		super(name,host,entry, protocol);
	}

	@Override
	public void turnOn() { 
 		if( RadarSystemConfig.withContext )  sendCommandOnConnection(Utils.turnOnLed.toString());
 		else sendCommandOnConnection( "on" );
 	}

	@Override
	public void turnOff() {   
 		if( RadarSystemConfig.withContext )  sendCommandOnConnection(Utils.turnOffLed.toString());
 		else sendCommandOnConnection( "off" );
 	}

	@Override
	public boolean getState() {   
		String answer="";
		if( RadarSystemConfig.withContext ) {
			answer = sendRequestOnConnection(Utils.getLedState.toString()) ;
		}
		else {
			answer = sendRequestOnConnection( "getState" );
			//Colors.out(name+" |  getState answer " + answer );
		}
		return answer.equals("true");
	}
}

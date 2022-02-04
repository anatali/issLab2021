package it.unibo.enablerCleanArch.enablers;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
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
  		if( RadarSystemConfig.protcolType == ProtocolType.tcp && RadarSystemConfig.withContext ) {
 			sendCommandOnConnection(Utils.turnOnLed.toString());
 		}
 		else if( RadarSystemConfig.protcolType == ProtocolType.mqtt) {
 			sendCommandOnConnection(Utils.turnOnLed.toString());
 		}
 		else if( RadarSystemConfig.protcolType == ProtocolType.coap) {
 			sendCommandOnConnection( "on" );
 		}else //CASO DI DEFAULT
 			sendCommandOnConnection( "on" );
 	}

	@Override
	public void turnOff() {   
		if( RadarSystemConfig.protcolType == ProtocolType.tcp && RadarSystemConfig.withContext ) {
 			sendCommandOnConnection(Utils.turnOffLed.toString());
 		}
 		else if( RadarSystemConfig.protcolType == ProtocolType.mqtt) {
 			sendCommandOnConnection(Utils.turnOffLed.toString());
 		}
 		else if( RadarSystemConfig.protcolType == ProtocolType.coap) {
 			sendCommandOnConnection( "off" );
 		} else  //CASO DI DEFAULT
 			sendCommandOnConnection( "off" );
 	}

	@Override
	public boolean getState() {   
		String answer="";
		if( RadarSystemConfig.protcolType == ProtocolType.tcp && RadarSystemConfig.withContext ) {
			answer = sendRequestOnConnection(Utils.buildRequest(name, "query", "getState", "led").toString()) ;
		}
  		else if( RadarSystemConfig.protcolType == ProtocolType.mqtt)  
  			answer = sendRequestOnConnection(Utils.buildRequest(name, "query", "getState", "led").toString());
		else { //CASO DI DEFAULT
			answer = sendRequestOnConnection( "getState" );
			//ColorsOut.out(name+" |  getState answer=" + answer, ColorsOut.BLUE );
		}
		return answer.equals("true");
	}
}

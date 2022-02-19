package it.unibo.enablerCleanArch.enablers;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
 
/*
 * Adapter for the output device  Led
 */
public class LedProxyAsClient extends ProxyAsClient implements ILed {

 	public LedProxyAsClient( String name, String host, String entry  ) {		
		this(name, host, entry, RadarSystemConfig.protcolType);
	}

	public LedProxyAsClient( String name, String host, String entry, ProtocolType protocol  ) {
		super(name,host,entry, protocol);
	}

	@Override
	public void turnOn() { 
  		if( RadarSystemConfig.withContext || Utils.isMqtt() || Utils.isCoap() ) {
 			sendCommandOnConnection(Utils.turnOnLed.toString());
// 		}
// 		else if( Utils.isMqtt() ) {
// 			sendCommandOnConnection(Utils.turnOnLed.toString());
// 		}
// 		else if( Utils.isCoap()) {
// 			sendCommandOnConnection( "on" );
 		}else //CASO DI DEFAULT
 			sendCommandOnConnection( "on" );
 	}

	@Override
	public void turnOff() {   
		if( Utils.isTcp() && RadarSystemConfig.withContext ) {
 			sendCommandOnConnection(Utils.turnOffLed.toString());
 		}
 		else if( Utils.isMqtt() ) {
 			sendCommandOnConnection(Utils.turnOffLed.toString());
 		}
 		else if( Utils.isCoap() ) {
 			sendCommandOnConnection( "off" );
 		} else  //CASO DI DEFAULT
 			sendCommandOnConnection( "off" );
 	}

	@Override
	public boolean getState() {   
		String answer="";
		if( Utils.isTcp() && RadarSystemConfig.withContext ) {
			answer = sendRequestOnConnection(Utils.buildRequest(name, "query", "getState", "led").toString()) ;
		}
  		else if( Utils.isMqtt() )  
  			answer = sendRequestOnConnection(Utils.buildRequest(name, "query", "getState", "led").toString());
		else { //CASO DI DEFAULT
			answer = sendRequestOnConnection( "getState" );
			//ColorsOut.out(name+" |  getState answer=" + answer, ColorsOut.BLUE );
		}
		return answer.equals("true");
	}
}

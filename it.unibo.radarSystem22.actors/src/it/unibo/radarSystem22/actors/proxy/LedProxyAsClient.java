package it.unibo.radarSystem22.actors.proxy;

import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.radarSystem22.interfaces.ILed;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;
 

/*
 * Adapter for the output device  Led
 */
public class LedProxyAsClient extends ProxyAsClient implements ILed {
public static final IApplMessage turnOnLed   = new ApplMessage("msg( cmd, dispatch, system, led, turnOn, 0 )");
public static final IApplMessage turnOffLed  = new ApplMessage("msg( cmd, dispatch, system, led, turnOff, 0 )");

 	public LedProxyAsClient( String name, String host, String entry  ) {		
		this(name, host, entry, CommSystemConfig.protcolType);
	}

	public LedProxyAsClient( String name, String host, String entry, ProtocolType protocol  ) {
		super(name,host,entry, protocol);
	}

	@Override
	public void turnOn() { 
  		if( CommSystemConfig.withContext || CommUtils.isMqtt() || CommUtils.isCoap() ) {
 			sendCommandOnConnection(  turnOnLed.toString());
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
  		if( CommSystemConfig.withContext || CommUtils.isMqtt() || CommUtils.isCoap() ) {
 			sendCommandOnConnection( turnOffLed.toString());
// 		}
// 		else if( Utils.isMqtt() ) {
// 			sendCommandOnConnection(Utils.turnOffLed.toString());
// 		}
// 		else if( Utils.isCoap() ) {
// 			sendCommandOnConnection( "off" );
 		} else  //CASO DI DEFAULT
 			sendCommandOnConnection( "off" );
 	}

	@Override
	public boolean getState() {   
		String answer="";
		if( CommUtils.isTcp() && CommSystemConfig.withContext ) {
			answer = sendRequestOnConnection(CommUtils.buildRequest(name, "query", "getState", "led").toString()) ;
		}
//  		else if( Utils.isMqtt() )  
//  			answer = sendRequestOnConnection(Utils.buildRequest(name, "query", "getState", "led").toString());
		else { //CASO DI DEFAULT
			answer = sendRequestOnConnection( "getState" );
			//ColorsOut.out(name+" |  getState answer=" + answer, ColorsOut.BLUE );
		}
		return answer.equals("true");
	}
}

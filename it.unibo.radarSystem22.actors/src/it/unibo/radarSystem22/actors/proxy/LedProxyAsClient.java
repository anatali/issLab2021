package it.unibo.radarSystem22.actors.proxy;

import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.domain.interfaces.*;
 

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
  		if(   CommUtils.isTcp() || CommUtils.isUdp() ) {
 			sendCommandOnConnection(  turnOnLed.toString());
  		}
 	}

	@Override
	public void turnOff() {   
  		if(  CommUtils.isTcp() || CommUtils.isUdp() ) {
 			sendCommandOnConnection( turnOffLed.toString());
  		}
  	}

	@Override
	public boolean getState() {   
		String answer="";
		if( CommUtils.isTcp() || CommUtils.isUdp()   ) {
			answer = sendRequestOnConnection(
					CommUtils.buildRequest(name, "query", "getState", "led").toString()) ;
		}
 		return answer.equals("true");
	}
}

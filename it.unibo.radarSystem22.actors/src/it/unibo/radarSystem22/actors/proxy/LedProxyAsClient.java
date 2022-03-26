package it.unibo.radarSystem22.actors.proxy;

import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
 

/*
 * Adapter for the output device  Led
 */
public class LedProxyAsClient extends ProxyAsClient implements ILed {
	protected   IApplMessage turnOnLed  ;  
	protected   IApplMessage turnOffLed ;
	protected   IApplMessage getState ;

 	public LedProxyAsClient( String name, String host, String entry  ) {		
		this(name, host, entry, CommSystemConfig.protcolType);
	}

	public LedProxyAsClient( String name, String host, String entry, ProtocolType protocol  ) {
		super(name,host,entry, protocol);
		turnOnLed  = CommUtils.buildDispatch(name, "cmd", "turnOn",  "led");
		turnOffLed = CommUtils.buildDispatch(name, "cmd", "turnOff", "led");
		getState   = CommUtils.buildRequest(name,  "req", "getState","led");
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
		String answerMsg="";
		String answer="";
		if( CommUtils.isTcp() || CommUtils.isUdp()   ) {
			answerMsg = sendRequestOnConnection( getState.toString()) ;
			answer    = new ApplMessage( answerMsg ).msgContent();
			ColorsOut.out("answer=" + answer , ColorsOut.MAGENTA);
		}
 		return answer.equals("true");
	}
}

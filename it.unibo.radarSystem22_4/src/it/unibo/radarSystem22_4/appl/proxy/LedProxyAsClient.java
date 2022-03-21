package it.unibo.radarSystem22_4.appl.proxy;

import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22_4.comm.ApplMessage;
import it.unibo.radarSystem22_4.comm.ProtocolType;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMessage;
import it.unibo.radarSystem22_4.comm.proxy.ProxyAsClient;
import it.unibo.radarSystem22_4.comm.utils.CommSystemConfig;
import it.unibo.radarSystem22_4.comm.utils.CommUtils;

/*
 * Adapter for the output device  Led
 */
public class LedProxyAsClient extends ProxyAsClient implements ILed {
	protected static IApplMessage turnOnLed ;//   = new ApplMessage("msg( cmd, dispatch, system, led, turnOn, 0 )");
	protected static IApplMessage turnOffLed; //   = new ApplMessage("msg( cmd, dispatch, system, led, turnOff, 0 )");
	protected static IApplMessage getLedState; //  = new ApplMessage("msg( cmd, request,  system, led, getState, 0 )");
	 
 	
 	public LedProxyAsClient( String name, String host, String entry  ) {		
		this(name, host, entry, ProtocolType.tcp);
	}

	public LedProxyAsClient( String name, String host, String entry, ProtocolType protocol  ) {
		super(name,host,entry, protocol);
   	    turnOnLed   = CommUtils.buildDispatch(name,"cmd", "on",      "led");
   	    turnOffLed  = CommUtils.buildDispatch(name,"cmd", "off",     "led");
   	    getLedState = CommUtils.buildRequest(name, "req", "getState","led");
	}

	@Override
	public void turnOn() { 
	     if( protocol == ProtocolType.tcp   ) {
	         sendCommandOnConnection(turnOnLed.toString());
	     }
	     //ALTRI PROTOCOLLI ...	     
  	}

	@Override
	public void turnOff() {   
	    if( protocol == ProtocolType.tcp   ) {
	    	sendCommandOnConnection( turnOffLed.toString());
	    }
	   //ALTRI PROTOCOLLI ...	 
 	}

	@Override
	public boolean getState() {   
		String answer = ""; 
	    if( protocol == ProtocolType.tcp   ) {
	    	answer = sendRequestOnConnection( getLedState.toString() );
			//ColorsOut.out(name+" |  getState answer=" + answer, ColorsOut.BLUE );
	    }
	    //ALTRI PROTOCOLLI ...	 
		return answer.equals("true");
	}
}

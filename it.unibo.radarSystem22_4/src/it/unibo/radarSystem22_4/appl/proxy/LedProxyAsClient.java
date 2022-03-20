package it.unibo.radarSystem22_4.appl.proxy;

import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22_4.appl.RadarSystemConfig;
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
	public static final IApplMessage turnOnLed    = new ApplMessage("msg( cmd, dispatch, system, led, turnOn, 0 )");
	public static final IApplMessage turnOffLed   = new ApplMessage("msg( cmd, dispatch, system, led, turnOff, 0 )");
 	public static final IApplMessage getLedState  = new ApplMessage("msg( cmd, request,  system, led, getState, 0 )");


 	public LedProxyAsClient( String name, String host, String entry  ) {		
		this(name, host, entry, ProtocolType.tcp);
	}

	public LedProxyAsClient( String name, String host, String entry, ProtocolType protocol  ) {
		super(name,host,entry, protocol);
	}

	@Override
	public void turnOn() { 
	     if( CommSystemConfig.protcolType==ProtocolType.tcp   ) {
	         sendCommandOnConnection(turnOnLed.toString());
	     }
//ALTRI PROTOCOLLI ...	     
//	       else if( CommUtils.isMqtt() ) {
//	         sendCommandOnConnection(turnOnLed.toString());
//	       }
//	       else if( CommUtils.isCoap() ) {
//	         sendCommandOnConnection( "on" );
//	       }else //CASO DI DEFAULT
//	         sendCommandOnConnection( "on" );
 	}

	@Override
	public void turnOff() {   
	     if( CommSystemConfig.protcolType==ProtocolType.tcp   ) {
 			sendCommandOnConnection( turnOffLed.toString());
	     }
 	}

	@Override
	public boolean getState() {   
		String answer = ""; 
	    if( CommSystemConfig.protcolType==ProtocolType.tcp   ) {
	    	answer = sendRequestOnConnection( "getState" );
			//ColorsOut.out(name+" |  getState answer=" + answer, ColorsOut.BLUE );
	    }
		return answer.equals("true");
	}
}

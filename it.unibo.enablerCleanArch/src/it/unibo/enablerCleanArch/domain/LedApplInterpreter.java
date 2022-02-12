package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
 
/*
 * TODO: il Led dovrebbe essere injected con un metodo o una annotation
 */
public class LedApplInterpreter implements IApplInterpreter  {
ILed led;
 
	public LedApplInterpreter(  ILed led) {
 		this.led = led;
	}

	public String elaborate( ApplMessage message ) {
		ColorsOut.out("LedApplInterpreter | elaborate ApplMessage=" + message  + " led="+led, ColorsOut.GREEN);
		String payload = message.msgContent();
		if(  message.isRequest() ) {
	 		if(payload.equals("getState") ) {
	 			String ledstate = ""+led.getState();
	 			ApplMessage reply = Utils.prepareReply( message, ledstate);
				return (reply.toString() ); //msg(...)
			}else return "request_unknown";
		}else return elaborate( payload );
	}
   
 	public String elaborate( String message ) {
		ColorsOut.out("LedApplInterpreter | elaborate String=" + message  + " led="+led, ColorsOut.GREEN);

	 		if( message.equals("getState") ) return ""+led.getState() ;
	 		else if( message.equals("on")) led.turnOn();
	 		else if( message.equals("off") ) led.turnOff();	
 
 		return message+"_done";
	}
}

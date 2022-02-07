package it.unibo.enablerCleanArchapplHandlers;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
 
/*
 * TODO: il Led dovrebbe essere injected con un metodo o una annotation
 */
public class LedApplLogic   {
ILed led;
 
	public LedApplLogic(  ILed led) {
 		this.led = led;
	}

	public String elaborate( ApplMessage message ) {
		ColorsOut.out("LedApplLogic | elaborate ApplMessage=" + message  + " led="+led, ColorsOut.GREEN);
		String payload = message.msgContent();
		if( ! message.isRequest() ) return "request_expected";
 		if(payload.equals("getState") ) {
 				String ledstate = ""+led.getState();
 				ApplMessage reply = Utils.prepareReply( message, ledstate);
				return (reply.toString() ); //msg(...)
		}else return elaborate( payload );
	}
   
	public String elaborate( String message ) {
		ColorsOut.out("LedApplLogic | elaborate String=" + message  + " led="+led, ColorsOut.GREEN);
 		if( message.equals("getState") ) return ""+led.getState() ;
 		else if( message.equals("on")) led.turnOn();
 		else if( message.equals("off") ) led.turnOff();	
 		return message+"_done";
	}
}

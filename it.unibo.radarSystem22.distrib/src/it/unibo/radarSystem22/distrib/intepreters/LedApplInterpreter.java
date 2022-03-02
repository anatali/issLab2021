package it.unibo.radarSystem22.distrib.intepreters;

import it.unibo.comm2022.interfaces.IApplInterpreter;
import it.unibo.comm2022.interfaces.IApplMessage;
import it.unibo.comm2022.utils.CommUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.interfaces.ILed;

/*
 * TODO: il Led dovrebbe essere injected con un metodo o una annotation
 */
public class LedApplInterpreter implements IApplInterpreter  {
ILed led;
 
	public LedApplInterpreter(  ILed led) {
 		this.led = led;
	}

	public String elaborate( IApplMessage message ) {
		ColorsOut.out("LedApplInterpreter | elaborate ApplMessage=" + message  + " led="+led, ColorsOut.GREEN);
		String payload = message.msgContent();
		if(  message.isRequest() ) {
	 		if(payload.equals("getState") ) {
	 			String ledstate = ""+led.getState();
	 			IApplMessage reply = CommUtils.prepareReply( message, ledstate);
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

package it.unibo.enablerCleanArchapplHandlers;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;


/*
 * TODO: il Led dovrebbe essere injected con un metodo o una annotation
 */
public class LedApplHandler extends ApplMsgHandler {
ILed led;

	public LedApplHandler(String name ) {
		super( name );
	}
	public LedApplHandler(String name, ILed led) {
		super(name);
		this.led = led;
	}
	
	public void setTheDevice( ILed dev ) {
		led = dev;
	}
	@Override
	public void elaborate( ApplMessage message, Interaction2021 conn ) {
		String payload = message.msgContent();
//		String sender  = message.msgSender();
//		String receiver= message.msgReceiver();
//		String reqId   = message.msgId();
		if( message.isRequest() ) {
//			if(payload.equals("getState") ) {
 				String ledstate = ""+led.getState();
//				ApplMessage reply = Utils.buildReply("led", reqId, ledstate, message.msgSender()) ;
 				ApplMessage reply = prepareReply( message, ledstate);
				sendAnswerToClient(reply.toString());
			
		}else if( message.isReply() ) {
			
		}else elaborate(payload, conn);
	}
	
 	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.out(name + " | elaborate message=" + message + " conn=" + conn + " led="+led, ColorsOut.GREEN);
 		if( message.equals("on")) led.turnOn();
 		else if( message.equals("off") ) led.turnOff();	
 		else if( message.equals("getState") ) sendMsgToClient(""+led.getState(), conn );
	}

}

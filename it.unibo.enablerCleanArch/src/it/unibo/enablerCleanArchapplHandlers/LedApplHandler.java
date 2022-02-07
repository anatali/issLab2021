package it.unibo.enablerCleanArchapplHandlers;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.IApplLogic;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.LedApplLogic;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.coap.LedResourceCoap;
import it.unibo.enablerCleanArch.supports.Utils;
 
/*
 * TODO: il Led dovrebbe essere injected con un metodo o una annotation
 */
public class LedApplHandler extends ApplMsgHandler {
private IApplLogic ledLogic;

	public static IApplMsgHandler create(String name, ILed led) {
		if( RadarSystemConfig.protcolType == ProtocolType.coap) {
			return new LedResourceCoap("led",  new LedApplLogic(led) );
		}else {
			return new LedApplHandler(name,led);
		}
	}
 
	public LedApplHandler(String name, ILed led) {
		super(name);
		ledLogic = new LedApplLogic(led) ;
 	}
	

	@Override
	public void elaborate( ApplMessage message, Interaction2021 conn ) {
		ColorsOut.out(name + " | elaborate ApplMessage=" + message + " conn=" + conn , ColorsOut.GREEN);
//		String payload = message.msgContent();
////		String sender  = message.msgSender();
////		String receiver= message.msgReceiver();
////		String reqId   = message.msgId();
//		if( message.isRequest() ) {
//			if(payload.equals("getState") ) {
// 				String ledstate = ""+led.getState();
// 				ApplMessage reply = prepareReply( message, ledstate);
//				sendAnswerToClient(reply.toString());
//			}
//		}else if( message.isReply() ) {
//			
//		}else elaborate(payload, conn);
		
		if( message.isRequest() ) {
			String answer = ledLogic.elaborate(message);
			if( Utils.isMqtt() ) sendAnswerToClient( answer  );
			else sendMsgToClient( answer, conn );
		}else {
			ledLogic.elaborate( message.msgContent() ); //non devo inviare risposta
		}	
	}
	
 	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.out(name + " | elaborate message=" + message + " conn=" + conn , ColorsOut.GREEN);
// 		if( message.equals("on")) led.turnOn();
// 		else if( message.equals("off") ) led.turnOff();	
 		if( message.equals("getState") ) sendMsgToClient( ledLogic.elaborate(message), conn );
 		else ledLogic.elaborate(message);
	}

}

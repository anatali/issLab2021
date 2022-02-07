package it.unibo.enablerCleanArchapplHandlers;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Interaction2021;
 
/*
 * TODO: il Led dovrebbe essere injected con un metodo o una annotation
 */
public class LedApplHandler extends ApplMsgHandler {
private LedApplLogic ledLogic;
 
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
			//sendAnswerToClient( answer  );
			sendMsgToClient( answer, conn );
		}else {
			ledLogic.elaborate( message.msgContent() );
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

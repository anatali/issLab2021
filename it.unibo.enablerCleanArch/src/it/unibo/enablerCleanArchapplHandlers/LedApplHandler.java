package it.unibo.enablerCleanArchapplHandlers;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.IApplInterpreter;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.LedApplInterpreter;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.coap.LedResourceCoap;
import it.unibo.enablerCleanArch.supports.Utils;
 
public class LedApplHandler extends ApplMsgHandler {
private IApplInterpreter ledInterpr;

	public static IApplMsgHandler create(String name, ILed led) {
		if( Utils.isCoap() ) {
			return new LedResourceCoap("led",  new LedApplInterpreter(led) );
		}else {
			return new LedApplHandler(name,led);
		}
	}
 
	public LedApplHandler(String name, ILed led) {
		super(name);
		ledInterpr = new LedApplInterpreter(led) ;
 	}
	

	@Override
	public void elaborate( ApplMessage message, Interaction2021 conn ) {
		ColorsOut.out(name + " | elaborate ApplMessage=" + message + " conn=" + conn , ColorsOut.GREEN);
		if( message.isRequest() ) {
			String answer = ledInterpr.elaborate(message);
			if( Utils.isMqtt() ) sendAnswerToClient( answer  );
			else sendMsgToClient( answer, conn );
		}else {
			ledInterpr.elaborate( message.msgContent() ); //non devo inviare risposta
		}	
	}
	
 	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.out(name + " | elaborate message=" + message + " conn=" + conn , ColorsOut.GREEN);
 		if( message.equals("getState") ) 
 			sendMsgToClient( ledInterpr.elaborate(message), conn );
 		else ledInterpr.elaborate(message);
	}

}

package it.unibo.radarSystem22.distrib.handlers;

import it.unibo.comm2022.ApplMsgHandler;
import it.unibo.comm2022.interfaces.IApplInterpreter;
import it.unibo.comm2022.interfaces.IApplMessage;
import it.unibo.comm2022.interfaces.IApplMsgHandler;
import it.unibo.comm2022.interfaces.Interaction2021;
import it.unibo.radarSystem22.distrib.intepreters.LedApplInterpreter;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.comm2022.utils.CommUtils;
import it.unibo.radarSystem22.interfaces.ILed;

public class LedApplHandler extends ApplMsgHandler {
private IApplInterpreter ledInterpr;

	public static IApplMsgHandler create(String name, ILed led) {
//		if( Utils.isCoap() )  
//			return new LedResourceCoap("led",  new LedApplInterpreter(led) );
//		 else  
			return new LedApplHandler(name,led);
		 
	}
 
	public LedApplHandler(String name, ILed led) {
		super(name);
		ledInterpr = new LedApplInterpreter(led) ;
 	}
	

	@Override
	public void elaborate( IApplMessage message, Interaction2021 conn ) {
		ColorsOut.out(name + " | elaborate ApplMessage=" + message + " conn=" + conn , ColorsOut.GREEN);
		if( message.isRequest() ) {
			String answer = ledInterpr.elaborate(message);
			if( CommUtils.isMqtt() ) sendAnswerToClient( answer  );
			else 
				sendMsgToClient( answer, conn );
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

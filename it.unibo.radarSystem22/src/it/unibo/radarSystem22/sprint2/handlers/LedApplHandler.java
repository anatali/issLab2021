package it.unibo.radarSystem22.sprint2.handlers;

 
import it.unibo.comm2022.ApplMsgHandler;
import it.unibo.comm2022.interfaces.IApplMessage;
import it.unibo.comm2022.interfaces.IApplMsgHandler;
import it.unibo.comm2022.interfaces.Interaction2021;
import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
 

public class LedApplHandler extends ApplMsgHandler {

	public static IApplMsgHandler create(String name, ILed led) {
 		return new LedApplHandler(name,led);		 
	}
 
	public LedApplHandler(String name, ILed led) {
		super(name);
 	}
	

	@Override
	public void elaborate( IApplMessage message, Interaction2021 conn ) {
 	}
	
 	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.out(name + " | elaborate message=" + message + " conn=" + conn , ColorsOut.GREEN);
 		if( message.equals("getState") ) {
 		}else {}
	}

}

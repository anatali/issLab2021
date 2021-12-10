package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class LedApplHandler extends ApplMessageHandler {
ILed led;

	public LedApplHandler(String name) {
		super(name);
		led = it.unibo.enablerCleanArch.domain.LedModel.createLedMock();
	}

	@Override
	public void elaborate(String message) {
		 System.out.println(name + " | elaborate " + message);
		
	}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		System.out.println(name + " | elaborate " + message + " conn=" + conn);
 		if( message.equals("on")) led.turnOn();
 		else if( message.equals("off") ) led.turnOff();		
 		try {
			conn.forward("Led_" + led.getState() );
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}

}

package it.unibo.enablerCleanArch.enablers.devices;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class LedApplHandler extends ApplMsgHandler {
ILed led;

	public LedApplHandler(String name, ILed led) {
		super(name);
		this.led = led;
	}
	
 	@Override
	public void elaborate(String message, Interaction2021 conn) {
		Colors.out(name + " | elaborate message=" + message + " conn=" + conn + " led="+led, Colors.GREEN);
 		if( message.equals("on")) led.turnOn();
 		else if( message.equals("off") ) led.turnOff();	
 		else if( message.equals("getState") ) sendMsgToClient(""+led.getState(), conn );
	}

}

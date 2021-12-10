package it.unibo.enablerCleanArch.enablers;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;
 
/*
 * Deve inviare messaggi TCP
 */
public class LedEnablerAsServer  extends EnablerAsServer {  
ILed led;   

	public LedEnablerAsServer() {
		super();
	}
	public LedEnablerAsServer( String name,  int port, ProtocolType protocol, ILed led, ApplMessageHandler userDefHandler   )   {
		super(name, port, protocol,userDefHandler);  
		this.led = led;
 	}
	
	@Override		//from ApplMessageHandler
	public void elaborate(String message) {
 		System.out.println(name+" | elaborate:" + message + " led=" + led);
 		if( message.equals("on")) led.turnOn();
 		else if( message.equals("off") ) led.turnOff();
 		//Added after  TcpContextServer
 		try {
 			ApplMessage msg = new ApplMessage(message);
 			if( msg.msgContent().equals("on")) led.turnOn();
 			else if( msg.msgContent().equals("off")) led.turnOff();
 		}catch( Exception e) {
 			Colors.outerr(name+" | elaborate ERROR=" + e.getMessage() );
 		}
	}
	@Override
	public void elaborate(String message, Interaction2021 conn) {
		System.out.println(name+" | elaborate:" + message + " led=" + led + " conn:" + conn);
		elaborate(message);
		try {
			conn.forward("LedState="+led.getState() );
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}
	

}

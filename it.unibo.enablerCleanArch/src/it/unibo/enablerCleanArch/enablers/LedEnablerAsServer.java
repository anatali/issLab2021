package it.unibo.enablerCleanArch.enablers;

import it.unibo.enablerCleanArch.domain.ILed;
 
 
 

/*
 * Deve inviare messaggi TCP
 */
public class LedEnablerAsServer  extends EnablerAsServer {  
ILed led;   
	public LedEnablerAsServer( String name,  int port, ProtocolType protocol, ILed led  )   {
		super(name, port, protocol );  
		this.led = led;
 	}

	@Override		//from ApplMessageHandler
	public void elaborate(String message) {
 		System.out.println(name+" | elaborate:" + message);
 		if( message.equals("on")) led.turnOn();
 		else if( message.equals("off") ) led.turnOff();
	}
}

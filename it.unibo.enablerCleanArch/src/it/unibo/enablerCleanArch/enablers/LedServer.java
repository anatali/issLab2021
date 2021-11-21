package it.unibo.enablerCleanArch.enablers;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.LedConcrete;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpServer;
 
 

/*
 * Deve inviare messaggi TCP
 */
public class LedServer extends ApplMessageHandler  {
ILed led = LedConcrete.create();

	public LedServer(  int port  )   {
		super("LedServer");
		try {
 			new TcpServer( name+"Server", port,  this );
		} catch (Exception e) {
 			e.printStackTrace();
		} 	
	}
	
	@Override		//from ApplMessageHandler
	public void elaborate(String message) {
 		System.out.println(name+" | elaborate:" + message);
 		if( message.equals("on")) led.turnOn();
 		else if( message.equals("off") ) led.turnOff();
	}
 
}

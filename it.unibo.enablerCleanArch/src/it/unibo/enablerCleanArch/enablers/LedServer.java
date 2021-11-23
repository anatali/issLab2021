package it.unibo.enablerCleanArch.enablers;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.LedAbstract;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.TcpServer;
 
 

/*
 * Deve inviare messaggi TCP
 */
public class LedServer extends EnablerAsServer  {
ILed led = LedAbstract.createLedConcrete(); //Calls a factory method
	public LedServer( String name,  int port  )   {
		super(name, port );
 	}	
	@Override
 	public void setProtocolServer(int port, ApplMessageHandler enabler) throws Exception{
  		new TcpServer( name+"Server", port,  this );
 	}	
	@Override		//from ApplMessageHandler
	public void elaborate(String message) {
 		System.out.println(name+" | elaborate:" + message);
 		if( message.equals("on")) led.turnOn();
 		else if( message.equals("off") ) led.turnOff();
	}
}

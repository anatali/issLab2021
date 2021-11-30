package it.unibo.enablerCleanArch.enablers;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.LedModel;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.TcpServer;
 
 

/*
 * Deve inviare messaggi TCP
 */
public class LedServer extends EnablerAsServer  {
ILed led;
	public LedServer( String name,  int port  )   {
		super(name, port );
		if( ! RadarSystemConfig.simulation) {
			led = LedModel.createLedConcrete(); //Calls a factory method
		}else {
			led = LedModel.createLedMock();
		}
 	}	
	@Override
 	public void setProtocolServer(int port ) throws Exception{
  		new TcpServer( name+"Server", port,  this );
 	}	
	@Override		//from ApplMessageHandler
	public void elaborate(String message) {
 		System.out.println(name+" | elaborate:" + message);
 		if( message.equals("on")) led.turnOn();
 		else if( message.equals("off") ) led.turnOff();
	}
}

package it.unibo.enablerCleanArch.enablers;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.LedModel;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.TcpServer;
 
 

/*
 * Deve inviare messaggi TCP
 */
public class LedServer  { //implements EnablerAsServer 
ILed led;
	public LedServer( String name,  int port  )   {
		//super(name, port, null ); //TODO
		if( ! RadarSystemConfig.simulation) {
			led = LedModel.createLedConcrete(); //Calls a factory method
		}else {
			led = LedModel.createLedMock();
		}
 	}	
	 
	 
	
	/*
	@Override		//from ApplMessageHandler
	public void elaborate(String message) {
 		System.out.println(name+" | elaborate:" + message);
 		if( message.equals("on")) led.turnOn();
 		else if( message.equals("off") ) led.turnOff();
	}*/
}

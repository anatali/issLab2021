package it.unibo.enablerCleanArch.enablers;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClient;
import it.unibo.enablerCleanArch.supports.TcpConnection;

/*
 * 

class LedMsgHandler extends ApplMessageHandler{
	private ILed led ;
	
	public LedMsgHandler(boolean simulated) {
		if( simulated ) led = new LedMock();
		else led = new LedConcrete();		
	}
	
	@Override
	protected void elaborate(String message) {
		//System.out.println("LedMsgHandler | elaborate " + message);
		if( message.equals("on") ) led.turnOn();
		else if( message.equals("off") ) led.turnOff();
	}

}	 */

/*
 * Deve inviare messaggi TCP
 */
public class LedServer  implements ILed{
private Interaction2021 conn;

	public LedServer(  int port  )   {
		 try {
			 //conn = TcpClient.connect(host,  port);
			 
		 }catch( Exception e) {
			 
		 }
	}

	@Override
	public void turnOn() {
		 //conn.forward("on");		
	}

	@Override
	public void turnOff() {
	}

	@Override
	public boolean getState() {
		 
		return false;
	}
}

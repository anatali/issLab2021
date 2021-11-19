package it.unibo.radarWithEnabler;
import it.unibo.bls.devices.LedConcrete;
import it.unibo.bls.devices.LedMock;
import it.unibo.bls.interfaces.ILed;
import it.unibo.enabler.ApplMessageHandler;
import it.unibo.enabler.TcpEnabler;

/*
 * 
 */
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

}	

/*
 * Tcp enabler for the LED
 * For each message sent to this enabler, the elaborate method of LedMsgHandler is called
 */
public class LedEnabler extends TcpEnabler{
	public LedEnabler(int port, boolean simulated ) throws Exception {
		super("ledEnabler",port, new LedMsgHandler(simulated));
	}
}

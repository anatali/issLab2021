package it.unibo.enabler;
import it.unibo.bls.devices.LedConcrete;
import it.unibo.bls.devices.LedMock;
import it.unibo.bls.interfaces.ILed;

public class LedEnabler extends TcpEnabler{
private ILed led ;

	public LedEnabler(int port) throws Exception {
		super("ledEnabler",port);
		//led = new LedMock();
		led = new LedConcrete();
	}


	@Override
	protected void elaborate(String message) {
		//System.out.println("LedEnabler | elaborate " + message);
		if( message.equals("on") ) led.turnOn();
		else if( message.equals("off") ) led.turnOff();
	}
	

	
}

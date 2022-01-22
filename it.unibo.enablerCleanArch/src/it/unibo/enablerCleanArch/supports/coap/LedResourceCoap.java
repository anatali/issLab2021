package it.unibo.enablerCleanArch.supports.coap;
import java.net.InetAddress;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.supports.Colors;

public class LedResourceCoap extends CoapDeviceResource {
	private ILed led; 
	
	public LedResourceCoap(String name, ILed led ) {
		super(name, DeviceType.output);
		this.led = led;
 	}

	@Override
	protected String elaborateGet(String req) {
		Colors.out( getName() + " |  before elaborateGet req:" + req + " led:" + led.getState()  );
 		return ""+led.getState();
	}

	@Override
	protected void elaboratePut(String req) {
		Colors.out( getName() + " |  before elaboratePut req:" + req + " led:" + led.getState()  );
		if( req.equals( "on") ) led.turnOn();
		else if( req.equals("off") ) led.turnOff();		
		//Colors.out( getName() + " |  after elaboratePut :" + led.getState()  );
	}  
	@Override
	protected void elaboratePut(String req, InetAddress callerAddr) {
		Colors.out( getName() + " | before elaboratePut req:" + req + " callerAddr="  + callerAddr  );
	}

}

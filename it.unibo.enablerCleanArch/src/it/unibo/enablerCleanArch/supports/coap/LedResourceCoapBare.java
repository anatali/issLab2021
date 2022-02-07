package it.unibo.enablerCleanArch.supports.coap;
import java.net.InetAddress;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.supports.ColorsOut;

public class LedResourceCoapBare extends CoapDeviceResourceBare {
	private ILed led; 
	
	public LedResourceCoapBare(String name, ILed led ) {
		super(name, DeviceType.output);
		this.led = led;
 	}

	@Override
	protected String elaborateGet(String req) {
		ColorsOut.out( getName() + " |  before elaborateGet req:" + req + " led:" + led.getState()  );
 		return ""+led.getState();
	}
	@Override
	protected String elaborateGet(String req, InetAddress callerAddr) {
		return elaborateGet(req);
	}

	@Override
	protected void elaboratePut(String req) {
		ColorsOut.out( getName() + " |  before elaboratePut req:" + req + " led:" + led.getState()  );
		if( req.equals( "on") ) led.turnOn();
		else if( req.equals("off") ) led.turnOff();		
		//Colors.out( getName() + " |  after elaboratePut :" + led.getState()  );
	}  
	@Override
	protected void elaboratePut(String req, InetAddress callerAddr) {
		ColorsOut.out( getName() + " | before elaboratePut req:" + req + " callerAddr="  + callerAddr  );
		elaboratePut(req);
	}

}

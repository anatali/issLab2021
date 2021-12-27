package it.unibo.enablerCleanArch.supports.coap;
import it.unibo.enablerCleanArch.domain.ILed;
 
 

public class LedResourceCoap extends CoapDeviceResource {
	private ILed led; 
	
	public LedResourceCoap(String name, ILed led ) {
		super(name, DeviceType.output);
		this.led = led;
 	}

	@Override
	protected String elaborateGet(String req) {
 		return ""+led.getState();
	}

	@Override
	protected void elaboratePut(String req) {
		//System.out.println( getName() + " |  before elaboratePut req:" + req + " led:" + led.getState()  );
		if( req.equals( "on") ) led.turnOn();
		else if( req.equals("off") ) led.turnOff();		
		//System.out.println( getName() + " |  after elaboratePut :" + led.getState()  );
	}  

}

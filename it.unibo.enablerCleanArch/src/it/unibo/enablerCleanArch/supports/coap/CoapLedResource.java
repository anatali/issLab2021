package it.unibo.enablerCleanArch.supports.coap;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.LedBuilder;
 

public class CoapLedResource extends CoapDeviceResource {
	private ILed led; 
	
	public CoapLedResource(String name) {
		super(name,deviceType.output);
		led = LedBuilder.create();
 	}

	@Override
	protected String elaborateGet(String req) {
 		return ""+led.getState();
	}

	@Override
	protected void elaboratePut(String req) {
		if( req == "on") led.turnOn();
		else if( req == "off") led.turnOff();		
	}  

}

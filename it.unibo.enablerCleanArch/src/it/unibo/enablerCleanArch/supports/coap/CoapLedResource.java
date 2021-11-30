package it.unibo.enablerCleanArch.supports.coap;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.LedModel;
 

public class CoapLedResource extends CoapDeviceResource {
	private ILed led; 
	
	public CoapLedResource(String name) {
		super(name,deviceType.output);
		led = LedModel.create();
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

package it.unibo.enablerCleanArch.enablers.devices;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.supports.ApplMsgHandlerCoap;
import it.unibo.enablerCleanArch.supports.coap.DeviceType;

public class LedApplHandlerCoap extends ApplMsgHandlerCoap {
ILed led;

	public LedApplHandlerCoap(String name) {
		super(name, DeviceType.output);
		led = it.unibo.enablerCleanArch.domain.LedModel.createLedMock();
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

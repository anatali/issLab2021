package it.unibo.radarSystem22_4.appl.interpreter;

 
import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22_4.comm.interfaces.IApplInterpreter;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMessage;
 

/*
 * TODO: il Led dovrebbe essere injected con un metodo o una annotation
 */
public class LedApplInterpreter implements IApplInterpreter  {
private ILed led;
 
	public LedApplInterpreter(  ILed led) {
 		this.led = led;
	}

    @Override
 	public String elaborate( IApplMessage message ) {
		ColorsOut.out("LedApplInterpreter | elaborate String=" + message  + " led="+led, ColorsOut.GREEN);
	 	String payload = message.msgContent();
		if( payload.equals("getState") ) return ""+led.getState() ;
	 	else if( payload.equals("on"))   led.turnOn();
	 	else if( payload.equals("off") ) led.turnOff();	
 		return payload+"_done";
	}
}

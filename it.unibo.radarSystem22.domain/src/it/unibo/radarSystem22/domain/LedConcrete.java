package it.unibo.radarSystem22.domain;

import java.io.IOException;

import it.unibo.radarSystem22.interfaces.ILed;

public class LedConcrete extends LedModel implements ILed{
private Runtime rt  = Runtime.getRuntime();
 	
	@Override
	protected void ledActivate(boolean val) {
		try {
			if( val ) rt.exec( "sudo bash led25GpioTurnOn.sh" );
			else rt.exec( "sudo bash led25GpioTurnOff.sh" );
		} catch (IOException e) {
			System.out.println("LedConcrete | ERROR " +  e.getMessage());
		}
	}
}

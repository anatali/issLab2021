package it.unibo.enablerCleanArch.domain;

import java.io.IOException;

public class LedConcrete extends LedAbstract implements ILed{
private Runtime rt      = Runtime.getRuntime();
 
	@Override
	protected void ledSetUp() {
		 turnOff();
	}
	
	@Override
	public void turnOn(){
		try {
			super.turnOn();
			rt.exec( "sudo bash led25GpioTurnOn.sh" );
		} catch (IOException e) {
			System.out.println("LedConcrete | ERROR " +  e.getMessage());
		}
	}
	@Override
	public void turnOff() {
		try {
			super.turnOff();
			rt.exec( "sudo bash led25GpioTurnOff.sh" );
		} catch (IOException e) {
			System.out.println("LedConcrete | ERROR " +  e.getMessage());
		}
	}
 



}

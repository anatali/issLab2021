package it.unibo.enablerCleanArch.domain;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;

public abstract class LedModel implements ILed{
	private boolean state = false;	
	
	public static ILed create() {
		ILed led ; 
		if( RadarSystemConfig.simulation ) led = createLedMock();
		else led = createLedConcrete();
		led.turnOff();		//initial state
		return led;
	}
	
	public static ILed createLedMock() {
		//return new LedMock();
		return LedMockWithGui.createLed();
	}
	public static ILed createLedConcrete() {
		return new LedConcrete();
	}	
	
	protected abstract void ledActivate( boolean val);
	
	protected void setState( boolean val ) {
		state = val;
		ledActivate( state );
	}
		
	@Override
	public void turnOn(){
		setState( true );
	}
	@Override
	public void turnOff() {
		setState(  false );		
	}
	@Override
	public boolean getState(){
		return state;
	}
	
}

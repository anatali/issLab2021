package it.unibo.enablerCleanArch.domain;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public abstract class LedModel implements ILed{
	protected boolean state = false;	
	
	public static ILed create() {
		if( RadarSystemConfig.simulation ) return createLedMock();
		else return createLedConcrete();
	}
	
	public static ILed createLedMock() {
		return new LedMock();
	}
	public static ILed createLedConcrete() {
		return new LedConcrete();
	}	
	
	protected abstract void ledSetUp() ;
	
	
	@Override
	public void turnOn(){
		state = true;
		showState();
	}
	@Override
	public void turnOff() {
		state = false;
		showState();
	}
	@Override
	public boolean getState(){
		return state;
	}

	private void showState(){
		System.out.println("Led state=" + state);
	}	
}

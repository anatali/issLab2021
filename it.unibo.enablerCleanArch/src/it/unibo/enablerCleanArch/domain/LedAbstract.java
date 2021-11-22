package it.unibo.enablerCleanArch.domain;

public abstract class LedAbstract implements ILed{
	protected boolean state = false;	
	
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

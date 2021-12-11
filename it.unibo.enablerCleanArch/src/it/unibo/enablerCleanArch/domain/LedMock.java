package it.unibo.enablerCleanArch.domain;

public class LedMock extends LedModel implements ILed{

	@Override
	protected void ledActivate(boolean val) {	
		//showState();
	}

	protected void showState(){
		System.out.println("LedMock inititial state=" + getState() );
	}
} 
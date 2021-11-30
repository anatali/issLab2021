package it.unibo.enablerCleanArch.domain;

public class LedMock extends LedModel implements ILed{

 
	@Override
	protected void ledSetUp() {
		 turnOff();
	}
}

package it.unibo.enablerCleanArch.domain;

public class LedMock extends LedBuilder implements ILed{

 
	@Override
	protected void ledSetUp() {
		 turnOff();
	}
}

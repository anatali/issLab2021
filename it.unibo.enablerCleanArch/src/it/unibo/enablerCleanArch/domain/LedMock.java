package it.unibo.enablerCleanArch.domain;

public class LedMock extends LedAbstract implements ILed{

 
	@Override
	protected void ledSetUp() {
		 turnOff();
	}
}

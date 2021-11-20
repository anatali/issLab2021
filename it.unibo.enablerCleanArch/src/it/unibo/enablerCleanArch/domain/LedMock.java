package it.unibo.enablerCleanArch.domain;

public class LedMock implements ILed{
protected boolean state = false;

//Factory method
public static ILed create(){
	return new LedMock();
}
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
	System.out.println("LedMock state=" + state);
}
}

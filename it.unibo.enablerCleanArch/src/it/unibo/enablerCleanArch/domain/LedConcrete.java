package it.unibo.enablerCleanArch.domain;

import java.io.IOException;

public class LedConcrete implements ILed{
private Runtime rt      = Runtime.getRuntime();
protected boolean state = false;

//Factory method
public static ILed create(){
	return new LedConcrete();
}
@Override
public void turnOn(){
	try {
		state = true;
		rt.exec( "sudo bash led25GpioTurnOn.sh" );
		showState();
	} catch (IOException e) {
		e.printStackTrace();
	}
}
@Override
public void turnOff() {
	try {
		state = false;
		rt.exec( "sudo bash led25GpioTurnOff.sh" );
		showState();
	} catch (IOException e) {
		e.printStackTrace();
	}

}
@Override
public boolean getState(){
	return state;
}

private void showState(){
	System.out.println("Led state=" + state);
}


}

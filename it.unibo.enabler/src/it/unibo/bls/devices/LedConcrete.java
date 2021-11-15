package it.unibo.bls.devices;

import java.io.IOException;

import it.unibo.bls.interfaces.ILed;

public class LedConcrete implements ILed{
private Runtime rt      = Runtime.getRuntime();
protected boolean state = false;

//Factory method
public static ILed createLed(){
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

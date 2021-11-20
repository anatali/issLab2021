package it.unibo.enablerCleanArch.domain;

public interface ILed {
	public void turnOn();
	public void turnOff();
	public boolean getState();
}

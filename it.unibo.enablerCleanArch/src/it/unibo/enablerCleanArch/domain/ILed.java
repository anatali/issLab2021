package it.unibo.enablerCleanArch.domain;

public interface ILed extends IDevice{
	public void turnOn();
	public void turnOff();
	public boolean getState();
}

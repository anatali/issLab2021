package it.unibo.enablerCleanArch.domain;

import java.util.Observer;

public interface IObserver extends Observer{
	public void update( int value );
	//From Observer public void update(Observable o, Object news) {
}

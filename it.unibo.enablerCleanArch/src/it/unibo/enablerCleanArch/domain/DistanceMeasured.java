package it.unibo.enablerCleanArch.domain;

import java.util.Observable;
import java.util.Observer;

import it.unibo.enablerCleanArch.supports.ColorsOut;

/*
 * Decorator
 */
public class DistanceMeasured extends Observable implements IDistanceMeasured{
private IDistance d;

	//public DistanceMeasured() {}
	@Override
	public void setVal( IDistance v ) {
		d = v;
 		setChanged();
		ColorsOut.out("DistanceMeasured setVal="+v + " obsNum=" + countObservers() + " hasChanged=" + hasChanged(), ColorsOut.MAGENTA);
	    
		notifyObservers( d );		
	}
	@Override
	public IDistance getDistance(   ) {
		return d;
	}	
	@Override
	public int getVal() { return d.getVal(); }
	
	@Override
	public String toString() {
		return ""+ getVal();
	}
  	
	@Override
	public void addObserver(Observer obs) {
		ColorsOut.out("DistanceMeasured addObserver="+obs , ColorsOut.MAGENTA);
		super.addObserver(obs);
		ColorsOut.out("DistanceMeasured addObserver obsNum=" + countObservers(), ColorsOut.RED);
 	}
 
}

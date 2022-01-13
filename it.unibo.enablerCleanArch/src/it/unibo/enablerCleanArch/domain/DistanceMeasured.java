package it.unibo.enablerCleanArch.domain;

import java.util.Observable;
import it.unibo.enablerCleanArch.supports.Colors;

/*
 * Decorator
 */
public class DistanceMeasured extends Observable implements IDistanceMeasured{
private IDistance d;

	public DistanceMeasured() {}
	@Override
	public void setVal( IDistance v ) {
		d = v;
		Colors.out("DistanceMeasured setVal="+v, Colors.ANSI_YELLOW);
		setChanged();
		Colors.out("DistanceMeasured setVal="+v + " hasChanged=" + hasChanged(), Colors.ANSI_YELLOW);
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
 
}

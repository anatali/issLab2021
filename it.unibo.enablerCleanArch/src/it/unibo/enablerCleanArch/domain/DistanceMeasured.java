package it.unibo.enablerCleanArch.domain;

import java.util.Observable;
import it.unibo.enablerCleanArch.supports.ColorsOut;

/*
 * Decorator
 */
public class DistanceMeasured extends Observable implements IDistanceMeasured{
private IDistance d;

	public DistanceMeasured() {}
	@Override
	public void setVal( IDistance v ) {
		d = v;
		ColorsOut.out("DistanceMeasured setVal="+v, ColorsOut.ANSI_YELLOW);
		setChanged();
		ColorsOut.out("DistanceMeasured setVal="+v + " hasChanged=" + hasChanged(), ColorsOut.ANSI_YELLOW);
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

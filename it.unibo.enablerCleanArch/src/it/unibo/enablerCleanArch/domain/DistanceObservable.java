package it.unibo.enablerCleanArch.domain;

import java.util.Observable;
import it.unibo.enablerCleanArch.supports.Colors;

/*
 * Decorator
 */
public class DistanceObservable extends Observable implements IDistance{
private IDistance d;

	public DistanceObservable( IDistance d ) { 
		this.d=d; 
		setChanged();
	    notifyObservers( ""+d.getVal() );		
	}
	@Override
	public void setVal( int v ) {
		d.setVal( v );
		//Colors.out("DistanceObservable setVal="+v);
		setChanged();
	    notifyObservers( ""+v );		
	}
	@Override
	public int getVal() { return d.getVal(); }
}

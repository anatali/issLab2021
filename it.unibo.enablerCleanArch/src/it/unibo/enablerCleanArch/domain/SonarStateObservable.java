package it.unibo.enablerCleanArch.domain;

import java.util.Observable;

import it.unibo.enablerCleanArch.supports.Colors;

public class SonarStateObservable extends Observable implements ISonarState{
private int v;

	public SonarStateObservable(int d) {
		v=d;
	}
	@Override
	public void setVal(int d) {
		v = d;
		//Colors.out("SonarStateObservable setVal="+v);
		setChanged();
	    notifyObservers(v);		
	}

	@Override
	public int getVal() {
		return v;
	}
}

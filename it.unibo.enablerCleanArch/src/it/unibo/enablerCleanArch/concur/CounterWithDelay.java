package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.supports.ColorsOut;

public class CounterWithDelay {
private String name;
private int n = 2;
	public CounterWithDelay( String name ) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void inc() { n = n + 1; }
	public void dec(int dt) {	//synchronized required BUT other clients delayed
		int v = n;
		v = v - 1;
		if( dt > 0 ) {
			ColorsOut.delay(dt);   //the control is given to another client
			ColorsOut.out(name + " | resumes v= " + v, ColorsOut.BLACK);
		}
		n = v;
		ColorsOut.out(name + " | new value after dec= " + n, ColorsOut.BLACK);
	}
	public int getVal() {
		return n;
	}
}

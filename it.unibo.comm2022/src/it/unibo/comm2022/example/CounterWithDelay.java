package it.unibo.comm2022.example;

import it.unibo.comm2022.utils.BasicUtils;
import it.unibo.comm2022.utils.ColorsOut;

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
 		BasicUtils.aboutThreads(name + " | CounterWithDelay before dec - ");
		int v = n;
		v = v - 1;
		if( dt > 0 ) {
			BasicUtils.delay(dt);   //the control is given to another client
			ColorsOut.outappl(name + " | resumes v= " + v, ColorsOut.BLACK);
		}
		n = v;
		ColorsOut.outappl(name + " | new value after dec= " + n, ColorsOut.BLACK);
		BasicUtils.aboutThreads(name + " | CounterWithDelay after dec - ");
	}
	public int getVal() {
		return n;
	}
}

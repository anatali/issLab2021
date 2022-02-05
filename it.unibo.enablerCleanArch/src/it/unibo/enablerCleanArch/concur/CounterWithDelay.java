package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.supports.ColorsOut;

public class CounterWithDelay {
private int n = 2;
	public void inc() { n = n + 1; }
	public void dec(int dt) {	//synchronized required BUT other clients delayed
		int v = n;
		v = v - 1;
		ColorsOut.delay(dt);   //the control is given to another client
		ColorsOut.out("Counter resumes v= " + v);
		n = v;
		ColorsOut.out("Counter new value after dec= " + n);
	}
}

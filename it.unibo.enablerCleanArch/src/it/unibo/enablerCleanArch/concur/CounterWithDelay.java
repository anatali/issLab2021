package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.supports.Colors;

public class CounterWithDelay {
private int n = 2;
	public void inc() { n = n + 1; }
	public synchronized void dec(int dt) {	//synchronized required BUT other clients delayed
		int v = n;
		v = v - 1;
		delay(dt);   //the control is given to another client
		Colors.out("Counter resumes v= " + v);
		n = v;
		Colors.out("Counter n= " + n);
	}
	
	private void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}	
}

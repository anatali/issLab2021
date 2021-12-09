package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.supports.Colors;

public class CounterWithDelay {
private int n = 2;
	public void inc() { n = n + 1; }
	public void dec(int dt) {
		int v = n;
		v = v - 1;
		delay(dt);
		n = v;
		Colors.out("Counter val = " + n);
	}
	
	private void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}	
}

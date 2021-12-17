package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;

import it.unibo.enablerCleanArch.domain.IDistance;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.supports.Colors;
 

class SonarConsumerForTesting extends Thread{
private ISonar sonar;
private int delta;
	public SonarConsumerForTesting( ISonar sonar, int delta) {
		this.sonar = sonar;
		this.delta = delta;
	}
	@Override
	public void run() {
		int v0 = sonar.getDistance().getVal();
		Colors.out("SonarConsumerForTesting | initial value v0=" + v0);

 		while( sonar.isActive() ) {
 			IDistance d = sonar.getDistance();
			int v       = d.getVal();
			Colors.out("SonarConsumerForTesting | v=" + v);
			int vexpectedMin = v0-delta;
			int vexpectedMax = v0+delta;
			assertTrue(  v <= vexpectedMax && v >= vexpectedMin );
			v0 = v;
		}
	}
}

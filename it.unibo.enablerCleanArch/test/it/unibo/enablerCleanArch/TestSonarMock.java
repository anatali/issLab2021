package it.unibo.enablerCleanArch;
import static org.junit.Assert.assertTrue;
import org.junit.*;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class TestSonarMock {
	@Before
	public void up() {
		System.out.println("up");
	}
	
	@After
	public void down() {
		System.out.println("down");		
	}	
	
	@Test 
	public void testSonarMock() {
		RadarSystemConfig.simulation = true;
		RadarSystemConfig.testing    = false;
		RadarSystemConfig.sonarDelay = 10;		//quite fast generation ...
		int delta = 1;
				
		ISonar sonar = DeviceFactory.createSonar();
		sonar.activate();
		
		int v0 = sonar.getDistance().getVal();
 		System.out.println("sonar initial value=" + v0);
 		
		while( sonar.isActive() ) {
			int d = sonar.getDistance().getVal();
	 		System.out.println("sonar getVal=" + d);
			int vexpectedMin = v0-delta;
			int vexpectedMax = v0+delta;
			assertTrue(  d <= vexpectedMax && d >= vexpectedMin );
			v0 = d;
		}		
	}
	
 
}

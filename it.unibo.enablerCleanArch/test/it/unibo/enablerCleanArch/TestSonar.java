package it.unibo.enablerCleanArch;
import static org.junit.Assert.assertTrue;
import org.junit.*;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.SonarMock;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class TestSonar {
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
		RadarSystemConfig.sonarDelay = 10;		//quite fast generation ...
		
		ISonar sonar = DeviceFactory.createSonar();
		sonar.activate();
		
		int v0 = sonar.getVal();
 		System.out.println("sonar v0=" + v0);
		while( sonar.isActive() ) {
			int d = sonar.getVal();
	 		//System.out.println("sonar getVal=" + d);
			int vexpected = v0-1;
			assertTrue(  d == vexpected );
			v0 = d;
		}
		
	}
	
 
}

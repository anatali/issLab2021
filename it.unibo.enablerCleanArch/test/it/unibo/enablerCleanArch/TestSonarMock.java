package it.unibo.enablerCleanArch;
import static org.junit.Assert.assertTrue;
import org.junit.*;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Utils;

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
		RadarSystemConfig.sonarDelay = 500;		//quite fast generation ...
		int delta = 1;
		
		ISonar sonar = DeviceFactory.createSonar();
		new SonarConsumerForTesting( sonar, delta ).start();  //consuma
		sonar.activate();		
 		while( sonar.isActive() ) { Utils.delay(1000);} //to avoid premature exit
 	}
	
 
}

package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.*;
import it.unibo.enablerCleanArch.domain.RadarGui;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.main.RadarSystemMainOnPc;

public class TestLedAlarm {
private RadarSystemMainOnPc sys;
	@Before
	public void setUp() {
		System.out.println("setUp");
		try {
			sys = new RadarSystemMainOnPc();
			sys.setup();
			RadarSystemConfig.testing = true;    //we must work with mock devices (local or remote)		
			//mock devices are 'almost identical' to concrete devices 
			sys.dowork();   //the sonar does not start if RadarSystemConfig.testing
			delay(5000);
		} catch (Exception e) {
			fail("setup ERROR " + e.getMessage() );
 		}
	}
	
	@After
	public void resetAll() {
		System.out.println("resetAll");		
	}	
	
	//@Test 
	public void testLedStartup() {
		System.out.println("testLedAlone");
		assertTrue( ! sys.getLed().getState() );
	}
	
	@Test 
	public void testLedAlarm() {
		System.out.println("testLedAlarm");
		
		int nearDistance = RadarSystemConfig.DLIMIT-5;
		sys.oneShotSonarForTesting(nearDistance);
		delay(1000);//give time the system to work. TODO: do it better
		//System.out.println("Led should be on: current state= "+sys.getLed().getState());
		RadarGui radar = (RadarGui) sys.getRadarGui();	//cast just for testing ...
	    assertTrue(  sys.getLed().getState() && radar.getCurDistance() == nearDistance);
	    
	    int farDistance = RadarSystemConfig.DLIMIT + 30;
		sys.oneShotSonarForTesting( farDistance );
		delay(1000);//give time the system to work. TODO: do it better
		//System.out.println("Led should be off: current state= "+sys.getLed().getState());
	    assertTrue( ! sys.getLed().getState() && radar.getCurDistance() == farDistance );
	}	
	
	
	private void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}
}

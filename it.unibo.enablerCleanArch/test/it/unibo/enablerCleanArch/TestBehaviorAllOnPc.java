package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.*;
import it.unibo.enablerCleanArch.domain.RadarGui;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.main.RadarSystemMainOnPc;

public class TestBehaviorAllOnPc {
private RadarSystemMainOnPc sys;
	@Before
	public void setUp() {
		System.out.println("setUp");
		try {
			sys = new RadarSystemMainOnPc();
			//Set system configuration (we don't read RadarSystemConfig.json)
			RadarSystemConfig.simulation 		= true;    
			//we must do testing work with mock devices (local or remote) ???
			//mock devices are 'almost identical' to concrete devices 
			RadarSystemConfig.testing    		= true;    		
			RadarSystemConfig.ControllerRemote	= false;    		
			RadarSystemConfig.LedRemote  		= false;    		
			RadarSystemConfig.SonareRemote  	= false;    		
			RadarSystemConfig.RadarGuieRemote  	= false;    	
			RadarSystemConfig.pcHostAddr        = "localhost";
			sys.setup();
			//sys.activateSonar();   //the sonar does not start if RadarSystemConfig.testing
			delay(5000);
		} catch (Exception e) {
			fail("setup ERROR " + e.getMessage() );
 		}
	}
	
	@After
	public void resetAll() {
		System.out.println("resetAll");		
	}	
	
	
	@Test 
	public void testLedAlarmAndRadarGui() {
		System.out.println("testLedAlarm");
/*		
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
	    */
	}	
	
	
	private void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}
}

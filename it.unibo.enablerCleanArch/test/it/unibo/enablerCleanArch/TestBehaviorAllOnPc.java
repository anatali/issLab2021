package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.*;
import it.unibo.enablerCleanArch.domain.RadarDisplay;
import it.unibo.enablerCleanArch.main.RadarSystemAllOnPc;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;


public class TestBehaviorAllOnPc {
private RadarSystemAllOnPc sys;
	@Before
	public void setUp() {
		System.out.println("setUp");
		try {
			//sys = new RadarSystemMainOnPc();
			sys = new RadarSystemAllOnPc();
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
			sys.build();
			//delay(5000);
		} catch (Exception e) {
			fail("setup ERROR " + e.getMessage() );
 		}
	}
	
	@After
	public void resetAll() {
		System.out.println("resetAll");		
	}	
	
	
	@Test 
	public void testFarDistance() {
		System.out.println("testFarDistance");
		//Obstacle far
 		RadarSystemConfig.testingDistance = RadarSystemConfig.DLIMIT +20;
		sys.activateSonar();   //il sonar produce un solo valore
		while( sys.getSonar().isActive() ) delay(10);   //give time the system to work 
		RadarDisplay radar = (RadarDisplay) sys.getRadarGui();	//cast just for testing ...
	    assertTrue( ! sys.getLed().getState() && radar.getCurDistance() == RadarSystemConfig.testingDistance );
	    delay(2000) ; //give time to look at the display
	}	
	
	@Test 
	public void testNearDistance() {
		System.out.println("testNearDistance");
		//Obstacle near
		RadarSystemConfig.testingDistance = RadarSystemConfig.DLIMIT - 1;
		sys.activateSonar();   //il sonar produce un solo valore
		while( sys.getSonar().isActive() ) delay(10); 	//give time the system to work 
		RadarDisplay radar = (RadarDisplay) sys.getRadarGui();	//cast just for testing ...
	    assertTrue(  sys.getLed().getState() && radar.getCurDistance() == RadarSystemConfig.testingDistance);
	    delay(2000) ; //give time to look at the display
	}	
	
	private void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}
}

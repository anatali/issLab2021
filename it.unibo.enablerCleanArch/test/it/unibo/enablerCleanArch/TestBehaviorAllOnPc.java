package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.*;
import it.unibo.enablerCleanArch.domain.RadarDisplay;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemAllOnPc;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Utils;


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
			RadarSystemConfig.protcolType       = ProtocolType.tcp;
			sys.build();
			Utils.delay(2000);
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
		RadarSystemConfig.testingDistance = RadarSystemConfig.DLIMIT +20;
		testTheDistance( false );
		/*
		System.out.println("testFarDistance");
		RadarDisplay radar = RadarDisplay.getRadarDisplay();  //singleton
		//Obstacle far
 		RadarSystemConfig.testingDistance = RadarSystemConfig.DLIMIT +20;
		sys.activateSonar();   //il sonar produce un solo valore
		while( sys.getSonar().isActive() ) Utils.delay(10);   //give time the system to work 
	    assertTrue( ! sys.getLed().getState() );
	    //Utils.delay(500) ;//give time to update the display
	    System.out.println("testFarDistance radar distance=" + radar.getCurDistance());
	    assertTrue( radar.getCurDistance() == RadarSystemConfig.testingDistance );
	    Utils.delay(1000) ; //give time to look at the display
	    */
	}	
	
	@Test 
	public void testNearDistance( ) {
		/*
		System.out.println("testNearDistance");
		RadarDisplay radar = RadarDisplay.getRadarDisplay();  //singleton
		//Obstacle near
		RadarSystemConfig.testingDistance = RadarSystemConfig.DLIMIT - 1;
		sys.activateSonar();   //il sonar produce un solo valore
		while( sys.getSonar().isActive() ) Utils.delay(10); 	//give time the system to work 
 	    assertTrue(  sys.getLed().getState() && radar.getCurDistance() == RadarSystemConfig.testingDistance);
	    Utils.delay(1000) ; //give time to look at the display
	    */
		RadarSystemConfig.testingDistance = RadarSystemConfig.DLIMIT - 1;
		testTheDistance( true );
	}	
	
	protected void testTheDistance( boolean ledStateExpected ) {
		System.out.println("testDistance " + RadarSystemConfig.testingDistance );
		RadarDisplay radar = RadarDisplay.getRadarDisplay();  //singleton
 		sys.activateSonar();   //il sonar produce un solo valore
		while( sys.getSonar().isActive() ) Utils.delay(10); 	//give time the system to work 
 	    assertTrue(  sys.getLed().getState() == ledStateExpected
 	    		&& radar.getCurDistance() == RadarSystemConfig.testingDistance);
	    Utils.delay(1000) ; //give time to look at the display		
	}
 
}

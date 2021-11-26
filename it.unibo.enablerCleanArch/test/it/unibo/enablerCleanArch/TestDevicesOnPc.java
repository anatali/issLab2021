package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.*;

import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.RadarGui;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.main.RadarSystemMainOnPc;

public class TestDevicesOnPc {
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
			//sys.dowork();   //the sonar does not start if RadarSystemConfig.testing
			//delay(2000);
		} catch (Exception e) {
			fail("setup ERROR " + e.getMessage() );
 		}
	}
	
	@After
	public void resetAll() {
		System.out.println("resetAll");		
	}	
	
	@Test 
	public void testDevices() {
		System.out.println("testDevices");
		assertTrue( ! sys.getLed().getState() );
		
		sys.getLed().turnOn();
		assertTrue(  sys.getLed().getState() );
		
		sys.getLed().turnOff();
		assertTrue(  ! sys.getLed().getState() );
		
		ISonar sonar = sys.getSonar();
		int simulatedTargetDistance = 15;
		sys.oneShotSonarForTesting(simulatedTargetDistance);
		int d = sonar.getVal();
		System.out.println("sonar getVal=" + d);
		assertTrue(  d == simulatedTargetDistance );
 
	}
	
 
}
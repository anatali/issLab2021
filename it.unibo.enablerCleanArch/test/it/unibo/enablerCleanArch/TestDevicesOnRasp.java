package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.*;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.main.RadarSystemMainOnPc;

public class TestDevicesOnRasp {
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
			RadarSystemConfig.LedRemote  		= true;    		
			RadarSystemConfig.SonareRemote  	= true;    		
			RadarSystemConfig.RadarGuieRemote  	= false;    	
			RadarSystemConfig.pcHostAddr        = "localhost";
			RadarSystemConfig.raspHostAddr	    = "192.168.1.10";
			sys.build();
			delay(3000);
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
		
		delay(1000);
		
		sys.getLed().turnOff();
		assertTrue(  ! sys.getLed().getState() );
 		
		ISonar sonar = sys.getSonar();
		System.out.println("testDevices sonar " + sonar);
		//int simulatedTargetDistance = 15;
		//sys.oneShotSonarForTesting(simulatedTargetDistance);
		//sonar.activate();
		//delay(1000);
		int d = sonar.getDistance();
		System.out.println("sonar getVal=" + d);
		assertTrue(  d > 0  );
 
	}
	
	private void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	} 
}

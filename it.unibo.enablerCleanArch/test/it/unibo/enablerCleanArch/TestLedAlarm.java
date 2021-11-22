package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.*;

import it.unibo.enablerCleanArch.main.RadarSystemMain;

public class TestLedAlarm {
private RadarSystemMain sys;
	@Before
	public void setUp() {
		System.out.println("setUp");
		try {
			sys = new RadarSystemMain();
			sys.setup();
			//wait until the system is started. TODO: do it better
			delay(3000);
		} catch (Exception e) {
			fail("setup ERROR " + e.getMessage() );
 		}
	}
	
	@After
	public void resetAll() {
		System.out.println("resetAll");
	}	
	
	//@Test 
	public void testLedAlone() {
		System.out.println("testLedAlone");
	}
	
	@Test 
	public void testLedAlarm() {
		System.out.println("testLedAlarm");
		sys.dowork();
		delay(10000);//give time the system to work. TODO: do it better
	}	
	
	
	private void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}
}

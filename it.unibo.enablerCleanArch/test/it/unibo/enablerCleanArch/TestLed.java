package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;
import org.junit.*;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;


public class TestLed {
 
	@Before
	public void setUp() {
		System.out.println("setUp");
	}
	
	@After
	public void setDown() {
		System.out.println("setDown");		
	}	
	
	@Test 
	public void testLedMock() {
		
		System.out.println("testLedMock");
		RadarSystemConfig.simulation = true; 
		
		ILed led = DeviceFactory.createLed();
		assertTrue( ! led.getState() );
		
 		led.turnOn();
		assertTrue(  led.getState() );
		
 		led.turnOff();
		assertTrue(  ! led.getState() );		
	}	
	
	@Test 
	public void testLedConcrete() {
		
		System.out.println("testLedConcrete");
		RadarSystemConfig.simulation = false; 
		
		ILed led = DeviceFactory.createLed();
		assertTrue( ! led.getState() );
		
 		led.turnOn();
		assertTrue(  led.getState() );
		
 		led.turnOff();
		assertTrue(  ! led.getState() );		
	}		
	
	private void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}
}

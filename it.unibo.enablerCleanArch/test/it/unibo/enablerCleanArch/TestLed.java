package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;
import org.junit.*;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Utils;


public class TestLed {
 
	@Before
	public void up() {
		System.out.println("up");
	}
	
	@After
	public void down() {
		System.out.println("down");		
	}	
	
	@Test 
	public void testLedMock() {
		
		System.out.println("testLedMock");
		RadarSystemConfig.simulation = true; 
		
		ILed led = DeviceFactory.createLed();
		assertTrue( ! led.getState() );
		
 		led.turnOn();
		assertTrue(  led.getState() );
		
		Utils.delay(1000);		//to see the ledgui
		
 		led.turnOff();
		assertTrue(  ! led.getState() );	
		
		Utils.delay(1000);		//to see the ledgui
	}	
	
	//@Test 
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
	
 
}

package it.unibo.radarSystem22.domain;
import static org.junit.Assert.assertTrue;
import org.junit.*;

import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.interfaces.ILed;
import it.unibo.radarSystem22.interfaces.ISonar;
 

public class TestController {
	private ILed led;
	private ISonar sonar;
	private Controller controller;
	
	@Before
	public void setup() {
		DomainSystemConfig.simulation = true;
		DomainSystemConfig.ledGui     = true;
		//RadarSystemConfig.tracing    = true;
		DomainSystemConfig.testing    = true;
		
		ColorsOut.out("setup with DLIMIT=" + DomainSystemConfig.DLIMIT , ColorsOut.GREEN);
		led        = DeviceFactory.createLed();
		sonar      = DeviceFactory.createSonar();
		controller = Controller.create(led, sonar);
		led.turnOff();
	}
	
	@After
	public void end() {
		ColorsOut.out("end");		
	}	
	
	@Test 
	public void testFarDistance() {
		DomainSystemConfig.testingDistance = DomainSystemConfig.DLIMIT +20;
		testTheDistance( false );
 	}	
	
	@Test 
	public void testNearDistance( ) {
 		DomainSystemConfig.testingDistance = DomainSystemConfig.DLIMIT - 1;
		testTheDistance( true );
	}	
	
	protected void testTheDistance( boolean ledStateExpected ) {
		ColorsOut.out("testDistance " + DomainSystemConfig.testingDistance, ColorsOut.GREEN );
		RadarDisplay radar = RadarDisplay.getRadarDisplay();  //singleton
		
		ActionFunction endFun = (n) -> { 
			ColorsOut.out(n);
			boolean ledState           = led.getState();
			int radarDisplayedDistance = radar.getCurDistance();
			
			ColorsOut.out("ledState=" + ledState + " radarDisplayedDistance=" + radarDisplayedDistance, ColorsOut.MAGENTA);
	 	    assertTrue(  ledState == ledStateExpected && 
	 	    		radarDisplayedDistance == DomainSystemConfig.testingDistance);
		};
		
		controller.start( endFun, 1 ); //one-shot
  	    BasicUtils.delay(3000) ; //give time to work ... 		
	}
 
}

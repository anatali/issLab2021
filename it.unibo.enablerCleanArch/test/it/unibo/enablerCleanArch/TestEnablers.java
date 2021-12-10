package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.enablerCleanArch.adapters.LedAdapterEnablerAsClient;
import it.unibo.enablerCleanArch.adapters.SonarAdapterEnablerAsServer;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.LedEnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarEnablerAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;

/*
 * Simulo un Controller su PC che usa
 * 	un SonarAdapterEnablerAsServer sulla porta 8013
 *  un LedAdapterEnablerAsClient 
 * Simulo un RaspberryPi che usa
 *  un SonarEnablerAsClient 
 *  un LedEnablerAsServer sulla porta 8015
 * 
 */
public class TestEnablers {
	
	private SonarAdapterEnablerAsServer sonarAdapterServer;
	private LedEnablerAsServer ledServer;
	private ILed ledClient;
	
	@Before
	public void setup() {
		RadarSystemConfig.simulation = true;
		RadarSystemConfig.ledPort    = 8015;
		RadarSystemConfig.sonarPort  = 8011;
		RadarSystemConfig.sonarDelay = 100;
 		RadarSystemConfig.testing    = false;

 		ISonar sonar = DeviceFactory.createSonar();
		ILed led     = DeviceFactory.createLed();
		
		/*
		 * Prima simulo la partenza dei server
		 */
		//Server su PC 
 		sonarAdapterServer = new SonarAdapterEnablerAsServer("SonarAdapterEnablerAsServer",RadarSystemConfig.sonarPort, ProtocolType.tcp );
 		//Server su Rasp 
 		ledServer          = new LedEnablerAsServer("LedEnablerAsServer",RadarSystemConfig.ledPort, ProtocolType.tcp, led );

 		//Client su PC
 		ledClient = new LedAdapterEnablerAsClient(
				"LedAdapterEnablerAsClient", "localhost",RadarSystemConfig.ledPort, ProtocolType.tcp );
 		//Client su Rasp  		
 		new SonarEnablerAsClient(
				"SonarEnablerAsClient", "localhost",RadarSystemConfig.sonarPort, ProtocolType.tcp, sonar);
		

	}

	@After
	public void down() {
		//System.out.println("down");		
		ledServer.deactivate();
		sonarAdapterServer.deactivate();
	}	
	
	
	@Test 
	public void testTheLed() {
		ledClient.turnOn();
		delay(500);
		assertTrue( ledClient.getState() );
		ledClient.turnOff();		
		delay(500);
		assertTrue( ! ledClient.getState() );
	}
	
	//@Test 
	public void testEnablers() {
		
		//Simulo il Controller
		sonarAdapterServer.activate();
		delay(500);
		
		System.out.println("testEnablers " + sonarAdapterServer.isActive());
		
		
		while( sonarAdapterServer.isActive() ) {
			int v = sonarAdapterServer.getVal();
			Colors.out("Controller-simulated getVal="+v, Colors.GREEN);
			delay(500);
			if( v < RadarSystemConfig.DLIMIT ) ledClient.turnOn();
			else ledClient.turnOff();
		}
		
		 
		
	}
	
	private void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}	
}

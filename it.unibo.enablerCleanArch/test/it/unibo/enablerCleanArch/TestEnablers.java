package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.devices.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.devices.LedApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.SonarApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.SonarProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

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
	
	private EnablerAsServer ledServer;
	private EnablerAsServer sonarServer;
	
 	private ILed ledClient;
	private ISonar sonarClient;
	
	@Before
	public void setup() {
		RadarSystemConfig.simulation = true;
		RadarSystemConfig.ledPort    = 8015;
		RadarSystemConfig.sonarPort  = 8011;
		RadarSystemConfig.sonarDelay = 100;
 		RadarSystemConfig.testing    = false;

 		ProtocolType protocol        = ProtocolType.tcp;
 		//ISonar sonar = DeviceFactory.createSonar();
		//ILed led     = DeviceFactory.createLed();
		
		/*
		 * Prima simulo la partenza dei server
		 */
		//Server su PC 
 		sonarServer = new EnablerAsServer(
 				"sonarServer",RadarSystemConfig.sonarPort,protocol, new SonarApplHandler("sonarH") );
 		//Server su Rasp 
 		ledServer   = new EnablerAsServer(
 				"ledServer",RadarSystemConfig.ledPort,protocol, new LedApplHandler("ledH")  );

 		//Client su PC
 		ledClient = new LedProxyAsClient(
				"ledClient", "localhost",""+RadarSystemConfig.ledPort, protocol );
 		//Client su Rasp  		
 		sonarClient = new SonarProxyAsClient(
				"sonarClient", "localhost",""+RadarSystemConfig.sonarPort, protocol );
		

	}

	@After
	public void down() {
		//System.out.println("down");		
		ledServer.deactivate();
		sonarServer.deactivate();
	}	
	
	
	//@Test 
	public void testTheLed() {
		ledClient.turnOn();
		Utils.delay(500);
		assertTrue( ledClient.getState() );
		ledClient.turnOff();		
		Utils.delay(500);
		assertTrue( ! ledClient.getState() );
	}
	
	@Test 
	public void testEnablers() {
		
		//Simulo il Controller
		sonarServer.activate();
		Utils.delay(500);		
		System.out.println("testEnablers " + sonarClient.isActive());
		
		
		while( sonarClient.isActive() ) {
			int v = sonarClient.getDistance().getVal();
			Colors.out("Controller-simulated getVal="+v, Colors.GREEN);
			Utils.delay(500);
			if( v < RadarSystemConfig.DLIMIT ) ledClient.turnOn();
			else ledClient.turnOff();
		}
		
		 
		
	}
	

}

package it.unibo.enablerCleanArch.enablers;

import static org.junit.Assert.assertTrue;

import org.eclipse.californium.core.server.resources.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.DeviceType;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;
import it.unibo.enablerCleanArchapplHandlers.SonarApplHandler;

/*
 * 	un Sonar Server sulla porta 8011
 *  un Led Server sulla porta 8015
 *  un Led Proxy 
 *  un Sonar Proxy
 * 
 */
public class TestEnablers {
	
	private ISonar sonar ;
	private ILed led;
	private EnablerAsServer ledServer;
	private EnablerAsServer sonarServer;	
 	private ILed ledPxy;
	private ISonar sonarPxy;
	private ProtocolType protocol = ProtocolType.tcp;
	
	@Before
	public void setup() {
		RadarSystemConfig.simulation = true;
		RadarSystemConfig.ledGui     = true;
		RadarSystemConfig.ledPort    = 8015;
		RadarSystemConfig.sonarPort  = 8011;
		RadarSystemConfig.sonarDelay = 100;
 		RadarSystemConfig.testing    = false;
 
   		sonar 	= DeviceFactory.createSonar();
		led     = DeviceFactory.createLed();
		
		/*
		 * I server
		 */
 	 	sonarServer = new EnablerAsServer("sonarSrv",
 	 			RadarSystemConfig.sonarPort,protocol, new SonarApplHandler("sonarH", sonar) );
	 	ledServer   = new EnablerAsServer("ledSrv",  
	 			RadarSystemConfig.ledPort,  protocol, new LedApplHandler("ledH", led)  );
 
		/*
		 * I client
		 */		
		String sonarUri   = CoapApplServer.inputDeviceUri+"/sonar";
		String entrySonar = protocol==ProtocolType.coap ? sonarUri : ""+RadarSystemConfig.sonarPort;
 		sonarPxy = new SonarProxyAsClient("sonarClient", "localhost",entrySonar, protocol );
		
		String ledUri     = CoapApplServer.outputDeviceUri+"/led";
		String entryLed   = protocol==ProtocolType.coap ? ledUri : ""+RadarSystemConfig.ledPort;
		ledPxy = new LedProxyAsClient("ledClient", "localhost", entryLed, protocol );	

	}

	@After
	public void down() {
		//System.out.println("down");		
		ledServer.stop();
		sonarServer.stop();
	}	
	
	
	//@Test 
	public void testTheLed() {
		protocol = ProtocolType.tcp;
		ledServer.start();
		
		ledPxy.turnOn();
		Utils.delay(500);
		assertTrue( ledPxy.getState() );
		ledPxy.turnOff();		
		Utils.delay(500);
		assertTrue( ! ledPxy.getState() );
		Utils.delay(500);
	}
	
	@Test 
	public void testEnablers() {
 		RadarSystemConfig.DLIMIT=30;
 		
		sonar.activate();
		sonarServer.start();
		ledServer.start();
		
		
		//Simulo il Controller
 		Utils.delay(500);		
		System.out.println("testEnablers " + sonarPxy.isActive());
		
		
		while( sonarPxy.isActive() ) {
			int v = sonarPxy.getDistance().getVal();
			ColorsOut.out("testEnablers getVal="+v, ColorsOut.GREEN);
			//Utils.delay(500);
			if( v < RadarSystemConfig.DLIMIT ) {
				ledPxy.turnOn();
				boolean ledState = ledPxy.getState();
				assertTrue( ledState );	
			}
			else {
				ledPxy.turnOff();
				boolean ledState = ledPxy.getState();
				assertTrue( ! ledState );	
			}
		}		
	}
}

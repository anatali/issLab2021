package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;

import org.eclipse.californium.core.server.resources.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.enablerCleanArch.domain.DeviceFactory;
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
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.DeviceType;

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
 	private ILed ledClient;
	private ISonar sonarClient;
	private ProtocolType protocol = ProtocolType.tcp;
	
	@Before
	public void setup() {
		RadarSystemConfig.simulation = true;
		RadarSystemConfig.ledPort    = 8015;
		RadarSystemConfig.sonarPort  = 8011;
		RadarSystemConfig.sonarDelay = 100;
 		RadarSystemConfig.testing    = false;
 
   		sonar 	= DeviceFactory.createSonar();
		led     = DeviceFactory.createLed();
		
		/*
		 * I server
		 */
 	 	sonarServer = new EnablerAsServer("sonarSrv",RadarSystemConfig.sonarPort,protocol, new SonarApplHandler("sonarH", sonar) );
	 	ledServer   = new EnablerAsServer("ledSrv",  RadarSystemConfig.ledPort,  protocol, new LedApplHandler("ledH", led)  );
 
		/*
		 * I client
		 */		
		String sonarUri   = CoapApplServer.inputDeviceUri+"/sonar";
		String entrySonar = protocol==ProtocolType.coap ? sonarUri : ""+RadarSystemConfig.sonarPort;
 		sonarClient = new SonarProxyAsClient("sonarClient", "localhost",entrySonar, protocol );
		
		String ledUri     = CoapApplServer.outputDeviceUri+"/led";
		String entryLed   = protocol==ProtocolType.coap ? ledUri : ""+RadarSystemConfig.ledPort;
		ledClient = new LedProxyAsClient("ledClient", "localhost", entryLed, protocol );	

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
		
		ledClient.turnOn();
		Utils.delay(500);
		assertTrue( ledClient.getState() );
		ledClient.turnOff();		
		Utils.delay(500);
		assertTrue( ! ledClient.getState() );
		Utils.delay(500);
	}
	
	@Test 
	public void testEnablers() {
 		sonar.activate();
		sonarServer.start();
		ledServer.start();
		
		RadarSystemConfig.testing=false; //true => oneshot
		RadarSystemConfig.sonarDelay=250;
		RadarSystemConfig.DLIMIT=30;
		
		//Simulo il Controller
 		Utils.delay(500);		
		System.out.println("testEnablers " + sonarClient.isActive());
		
		
		while( sonarClient.isActive() ) {
			int v = sonarClient.getDistance().getVal();
			Colors.out("testEnablers getVal="+v, Colors.GREEN);
			//Utils.delay(500);
			if( v < RadarSystemConfig.DLIMIT ) {
				ledClient.turnOn();
				boolean ledState = ledClient.getState();
				assertTrue( ledState );	
			}
			else {
				ledClient.turnOff();
				boolean ledState = ledClient.getState();
				assertTrue( ! ledState );	
			}
		}		
	}
}

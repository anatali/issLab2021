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
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.LedResourceCoap;
import it.unibo.enablerCleanArch.supports.coap.DeviceType;
import it.unibo.enablerCleanArch.supports.coap.SonarResourceCoap;

/*
 * 	un Sonar Server sulla porta 8011
 *  un Led Server sulla porta 8015
 *  un Led Proxy 
 *  un Sonar Proxy
 * 
 */
public class TestEnablersCoap {
	
	private ISonar sonar ;
	private ILed led;
  	private ILed ledClient;
	private ISonar sonarClient;
	private Resource sonarRes;
	private Resource ledRes;
	private ProtocolType protocol = ProtocolType.coap;
	
	@Before
	public void setup() {
		RadarSystemConfig.simulation = true;
		RadarSystemConfig.ledPort    = 8015;
		RadarSystemConfig.sonarPort  = 8011;
		RadarSystemConfig.sonarDelay = 250;
 		RadarSystemConfig.testing    = false;
 		RadarSystemConfig.DLIMIT     = 60;
 
   		sonar 	= DeviceFactory.createSonar();
		led     = DeviceFactory.createLed();
		
		/*
		 * Le risorse
		 */
 			 sonarRes = new SonarResourceCoap("sonar",sonar ) ;  
			 ledRes   = new LedResourceCoap("led", led) ;  
 
		/*
		 * I client
		 */		
		String sonarUri = CoapApplServer.inputDeviceUri+"/sonar";
  		sonarClient     = new SonarProxyAsClient("sonarClient", "localhost",sonarUri, protocol );
		
		String ledUri = CoapApplServer.outputDeviceUri+"/led";
 		ledClient     = new LedProxyAsClient("ledClient", "localhost", ledUri, protocol );	

	}

	@After
	public void down() {
		//System.out.println("down");		
	}	
	
	

	
	@Test 
	public void testEnablersCoap() {
// 		sonar.activate();
//		sonarServer.activate();
//		ledServer.activate();
		sonarClient.activate();
		
		RadarSystemConfig.testing=false; //true => oneshot
		RadarSystemConfig.sonarDelay=250;
		RadarSystemConfig.DLIMIT=30;
		
		//Simulo il Controller
 		Utils.delay(1500);		
		System.out.println("testEnablersCoap " + sonarClient.isActive());
		
		
		while( sonarClient.isActive() ) {
			int v = sonarClient.getDistance().getVal();
			Colors.out("testEnablersCoap getVal="+v, Colors.GREEN);
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
			if( v > 60 ) break;
		}		
	}
}

package it.unibo.enablerCleanArch.enablers;

import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;
import it.unibo.enablerCleanArchapplHandlers.SonarApplHandler;

/*
 * 	un Sonar Server sulla porta 8011
 *  un Led Server sulla porta 8015
 *  un Led Proxy 
 *  un Sonar Proxy
 * 
 */
public class TestEnablersTcp {
	
	private ISonar sonar ;
	private ILed led;
	private EnablerAsServer ledServer;
	private EnablerAsServer sonarServer;	
 	private ILed ledPxy;
	private ISonar sonarPxy;
	private ProtocolType protocol = ProtocolType.tcp;
	
	@Before
	public void setup() {
		RadarSystemConfig.withContext= false; 
		RadarSystemConfig.simulation = true;
		RadarSystemConfig.ledGui     = true;
		RadarSystemConfig.ledPort    = 8015;
		RadarSystemConfig.sonarPort  = 8011;
		RadarSystemConfig.sonarDelay = 100;
 		RadarSystemConfig.testing    = false;
 		RadarSystemConfig.tracing    = false;
 
 		//I devices
   		sonar 	= DeviceFactory.createSonar();
		led     = DeviceFactory.createLed();
		
 		//I server
  	 	sonarServer = new EnablerAsServer("sonarSrv",RadarSystemConfig.sonarPort,protocol, new SonarApplHandler("sonarH", sonar) );
	 	ledServer   = new EnablerAsServer("ledSrv",  RadarSystemConfig.ledPort,  protocol, new LedApplHandler("ledH", led)  );
 
 		//I client
   		sonarPxy = new SonarProxyAsClient("sonarPxy", "localhost", ""+RadarSystemConfig.sonarPort, protocol );		
 		ledPxy   = new LedProxyAsClient(  "ledPxy",   "localhost", ""+RadarSystemConfig.ledPort,   protocol );	

	}

	@After
	public void down() {
		System.out.println("down");		
		ledServer.stop();
		sonarServer.stop();
	}	
	
	
	//@Test 
	public void testTheLed() {
		ledServer.start();
		System.out.println(" ==================== testTheLed "  );
		
		ledPxy.turnOn();
		Utils.delay(500);
		assertTrue( ledPxy.getState() );
		ledPxy.turnOff();		
		Utils.delay(500);
		assertTrue( ! ledPxy.getState() );
		Utils.delay(1000);
	}
	
	@Test 
	public void testEnablers() {
		sonarServer.start();
		ledServer.start();
		System.out.println(" ==================== testEnablers "  );
		
//		RadarSystemConfig.testing=false; //true => oneshot
//		RadarSystemConfig.sonarDelay=250;
//		RadarSystemConfig.DLIMIT=30;
		
		//Simulo il Controller
 		Utils.delay(500);		
		
		//Attivo il sonar
		sonarPxy.activate();
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

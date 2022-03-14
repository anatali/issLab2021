package it.unibo.radarSystem22.sprint3;

import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.comm2022.ProtocolType;
import it.unibo.comm2022.enablers.EnablerAsServer;
import it.unibo.comm2022.utils.BasicUtils;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.sprint2.RadarSysConfigSprint2;
import it.unibo.radarSystem22.sprint2.handlers.LedApplHandler;
import it.unibo.radarSystem22.sprint2.handlers.SonarApplHandler;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.sprint2.proxy.LedProxyAsClient;
import it.unibo.radarSystem22.sprint2.proxy.SonarProxyAsClient;

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
  
 		//I devices
   		sonar 	= DeviceFactory.createSonar();
		led     = DeviceFactory.createLed();
		
 		//I server
  	 	sonarServer = new EnablerAsServer("sonarSrv",
  	 			RadarSysConfigSprint2.sonarPort,protocol, 
  	 			new SonarApplHandler("sonarH", sonar) );
	 	ledServer   = new EnablerAsServer("ledSrv",  
	 			RadarSysConfigSprint2.ledPort,  protocol, 
	 			new LedApplHandler("ledH", led)  );
 
 		//I client
   		sonarPxy = new SonarProxyAsClient("sonarPxy", "localhost", 
   				""+RadarSysConfigSprint2.sonarPort, protocol );		
 		ledPxy   = new LedProxyAsClient(  "ledPxy",   "localhost", 
 				""+RadarSysConfigSprint2.ledPort,   protocol );	

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
		BasicUtils.delay(500);
		assertTrue( ledPxy.getState() );
		ledPxy.turnOff();		
		BasicUtils.delay(500);
		assertTrue( ! ledPxy.getState() );
		BasicUtils.delay(1000);
	}
	
	@Test 
	public void testEnablers() {
		sonarServer.start();
		ledServer.start();
		System.out.println(" ==================== testEnablers "  );
		
//		RadarSysConfigSprint2.testing=false; //true => oneshot
//		RadarSysConfigSprint2.sonarDelay=250;
//		RadarSysConfigSprint2.DLIMIT=30;
		
		//Simulo il Controller
		BasicUtils.delay(500);		
		
		//Attivo il sonar
		sonarPxy.activate();
		System.out.println("testEnablers " + sonarPxy.isActive());
		
		while( sonarPxy.isActive() ) {
			int v = sonarPxy.getDistance().getVal();
			ColorsOut.out("testEnablers getVal="+v, ColorsOut.GREEN);
			//Utils.delay(500);
			if( v < RadarSysConfigSprint2.DLIMIT ) {
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

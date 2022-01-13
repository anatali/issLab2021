package it.unibo.enablerCleanArch.main;

import java.io.IOException;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;
import it.unibo.enablerCleanArchapplHandlers.RadarApplHandler;

/*
 * Applicazione che va in coppia con RadarSystemDevicesOnRasp
 */

public class RadarSystemMainOnPcMqtt implements IApplication{
private IRadarDisplay radar = null;
private ILed   ledClient;
private ISonar sonarClient;

private boolean ledblinking = false;

    public RadarSystemMainOnPcMqtt(){
    }
    
	@Override
	public String getName() {	 
		return "RadarSystemMainOnPcMqtt";
	}

	public void setup( String configFile )  {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
		else {
			RadarSystemConfig.simulation   		= false;
			RadarSystemConfig.testing      		= false;			
			RadarSystemConfig.DLIMIT      		= 12; //55
			RadarSystemConfig.protcolType       = ProtocolType.mqtt;
			//RadarSystemConfig.withContext       = true;
		}
 	}
	
	public void configure()  {			
 		//radar  = DeviceFactory.createRadarGui();	
		String host           = RadarSystemConfig.pcHostAddr;
		ProtocolType protocol = RadarSystemConfig.protcolType;
		String ctxTopic       = "topicCtxMqtt";
 		ledClient             = new LedProxyAsClient("clientLed", host, ctxTopic, protocol );
 		sonarClient           = new SonarProxyAsClient("clientSonar", host, ctxTopic, protocol );
	} 
	
	@Override
	public void doJob(String configFileName) {
		setup(configFileName);
		configure();    	
		execute();
	}

	
	public void ledActivate( boolean v ) {
		if( v ) ledClient.turnOn();
		else ledClient.turnOff();
	}
	
	public String ledState(   ) {
		return ""+ledClient.getState();
	}
	public String sonarDistance(   ) {
		return ""+sonarClient.getDistance();
	}
	
	public void doLedBlink() {
		new Thread() {
			public void run() {
				ledblinking = true;
				while( ledblinking ) {
					ledActivate(true);
					Utils.delay(500);
					ledActivate(false);
					Utils.delay(500);
				}
			}
		}.start();
	}
	public void stopLedBlink() {
		ledblinking = false;
	}
	
	public void execute() {
		//sonarClient.deactivate();
		
 		ledActivate(true);		
 		//Colors.outappl("Led state="+ledState(), Colors.GREEN);
  		Utils.delay(1000);
 		ledActivate(false);
		//Colors.outappl("Led state="+ledState(), Colors.GREEN);
 		//Utils.delay(5000);

//		doLedBlink();
//		Utils.delay(3000);
//		stopLedBlink();
		
//		String ledstate = ledState(   );
//		Colors.outappl("Led state="+ledstate, Colors.GREEN);
		
		try {
			Colors.outappl("Please hit to restart ", Colors.ANSI_PURPLE);
			int v = System.in.read();
		} catch (IOException e) {
				e.printStackTrace();
		}
		 
		//for( int i=1; i<=3; i++) {
			sonarClient.activate();
			while( ! sonarClient.isActive() ) {
				Colors.outappl("Sonar not active .. =", Colors.GREEN);
				Utils.delay(500);
			}
			
			//if( sonarClient.isActive() ) {
				for( int i=1; i<=10; i++) {
	 				String sonarstate = sonarDistance(   );
					Colors.outappl("Sonar state i=" + i + " -> "+sonarstate, Colors.GREEN);
					//Utils.delay(500);
				}
 			//}
				//Utils.delay(800);
			sonarClient.deactivate();
		//}
		terminate();
	}

	public void terminate() {
		System.exit(0);
	}

 
 	public IRadarDisplay getRadarGui() {
		return radar;
	}

	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainOnPcMqtt().doJob(null);
 	}

 
}

package it.unibo.enablerCleanArch.main;

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
private ILed   ledClient1;
private ISonar sonarClient;

private boolean ledblinking = false;

    public RadarSystemMainOnPcMqtt(){
		setup("RadarSystemConfig.json");
		configure();    	
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
		}
 	}
	
	public void configure()  {			
 		//radar  = DeviceFactory.createRadarGui();	
		String host           = RadarSystemConfig.pcHostAddr;
		ProtocolType protocol = RadarSystemConfig.protcolType;
		String ctxTopic       = "topicCtxMqtt";
 		ledClient1            = new LedProxyAsClient("client1", host, ctxTopic, protocol );
 		sonarClient           = new SonarProxyAsClient("clientSonar", host, ctxTopic, protocol );
	} 
	
	@Override
	public void doJob(String configFileName) {
		setup(configFileName);
		configure();
		//execute();
	}
	
	public void ledActivate( boolean v ) {
		if( v ) ledClient1.turnOn();
		else ledClient1.turnOff();
	}
	
	public String ledState(   ) {
		return ""+ledClient1.getState();
	}
	public String sonarState(   ) {
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
 		
//		ledActivate(true);		
//		Colors.outappl("Led state="+ledState(), Colors.GREEN);
// 		Utils.delay(2000);
// 		ledActivate(false);
//		Colors.outappl("Led state="+ledState(), Colors.GREEN);

//		doLedBlink();
//		Utils.delay(3000);
//		stopLedBlink();
		
//		String ledstate = ledState(   );
//		Colors.outappl("Led state="+ledstate, Colors.GREEN);
		
		sonarClient.activate();
		while( sonarClient.isActive() ) {
			Utils.delay(500);
			String sonarstate = sonarState(   );
			Colors.outappl("Sonar state="+sonarstate, Colors.GREEN);
			break;
		}
	}

	public void terminate() {
		System.exit(0);
	}

 
 	public IRadarDisplay getRadarGui() {
		return radar;
	}

	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainOnPcMqtt().execute(); //.doJob(null);
 	}

}

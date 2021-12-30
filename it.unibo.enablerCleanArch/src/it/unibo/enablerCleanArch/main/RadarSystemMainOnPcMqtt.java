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

	
	@Override
	public String getName() {	 
		return "RadarSystemMainOnPcMqtt";
	}

	public void setup( String configFile )  {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
		else {
			RadarSystemConfig.simulation   		= false;
//			RadarSystemConfig.raspHostAddr 		= "localhost"; //"192.168.1.183";
//			RadarSystemConfig.SonareRemote 		= true;
//			RadarSystemConfig.LedRemote    		= true;
//			RadarSystemConfig.ControllerRemote  = true;
//			RadarSystemConfig.sonarPort    		= 8012;
//			RadarSystemConfig.ledPort      		= 8010;
//			RadarSystemConfig.radarGuiPort    	= 8014;
//			RadarSystemConfig.withContext  		= true;
//			RadarSystemConfig.ctxServerPort		= 8018;
			RadarSystemConfig.testing      		= false;			
			RadarSystemConfig.DLIMIT      		= 12; //55
			RadarSystemConfig.protcolType       = ProtocolType.mqtt;
		}
 	}
	
	public void configure()  {			
 		//radar  = DeviceFactory.createRadarGui();	
 	} 
	
	@Override
	public void doJob(String configFileName) {
		setup(configFileName);
		configure();
		execute();
	}
	
	public void execute() {
		String host           = RadarSystemConfig.pcHostAddr;
		ProtocolType protocol = RadarSystemConfig.protcolType;
		String ctxTopic       = "topicCtxMqtt";
 		ILed ledClient1       = new LedProxyAsClient("client1", host, ctxTopic, protocol );
 		
 		ledClient1.turnOn();
 		Utils.delay(3000);
 		ledClient1.turnOff();

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

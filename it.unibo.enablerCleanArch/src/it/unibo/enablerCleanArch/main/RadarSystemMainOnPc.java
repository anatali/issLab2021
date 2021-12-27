package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.devices.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.devices.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

public class RadarSystemMainOnPc implements IApplication{
private IRadarDisplay radar = null;

	
	@Override
	public String getName() {	 
		return "RadarSystemMainOnPc";
	}

	public void setup( String configFile )  {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
		else {
			RadarSystemConfig.simulation   = false;
			RadarSystemConfig.raspHostAddr = "192.168.1.6";
			RadarSystemConfig.SonareRemote = true;
			RadarSystemConfig.LedRemote    = true;
			RadarSystemConfig.sonarPort    = 8012;
			RadarSystemConfig.ledPort      = 8010;
			RadarSystemConfig.sonarDelay   = 100;
			RadarSystemConfig.testing      = false;			
		}
 	}
	
	public void configure()  {			
 		radar  = DeviceFactory.createRadarGui();	
 	} 
	
	@Override
	public void doJob(String configFileName) {
		setup(configFileName);
		configure();
		execute();
		//Utils.delay(1500);
 		//terminate();		
	}
	
	public void execute() {
		ILed clientLedProxy = new LedProxyAsClient("clientLedProxy", 
				RadarSystemConfig.raspHostAddr, ""+RadarSystemConfig.ledPort, ProtocolType.tcp );
  		ISonar clientSonarProxy = new SonarProxyAsClient("clientSonarProxy", 
				RadarSystemConfig.raspHostAddr, ""+RadarSystemConfig.sonarPort, ProtocolType.tcp );
 		
  		//clientSonarProxy.activate(); //Activate the sonar

 		Controller.activate(clientLedProxy, clientSonarProxy, radar);
 		
 		
	}

	public void terminate() {
		System.exit(0);
	}

 
 	public IRadarDisplay getRadarGui() {
		return radar;
	}

	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainOnPc().doJob(null);
 	}

}

package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.adapters.LedAdapterClient;
import it.unibo.enablerCleanArch.adapters.RadarGuiAdapterServer;
import it.unibo.enablerCleanArch.adapters.SonarAdapterServer;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.RadarGuiClient;
 
public class RadarSystemMain {

	public void setup() throws Exception {
		RadarSystemConfig.setTheConfiguration(   );
		
		//Control
		if( RadarSystemConfig.ControllerRemote ) {
			DeviceFactory.createRadarGui();			
			new RadarGuiAdapterServer( RadarSystemConfig.radarGuiPort );
		}else {
			//Input
			ISonar sonar     = RadarSystemConfig.SonareRemote   ? new SonarAdapterServer("sonarAdapterServer") : DeviceFactory.createSonar();
			//Output
			ILed led         = RadarSystemConfig.LedRemote       ? new LedAdapterClient( ) : DeviceFactory.createLed();
			//IRadarGui radar  = RadarSystemConfig.RadarGuieRemote ? new RadarGuiAdapterClient(  ) : DeviceFactory.createRadarGui();
			IRadarGui radar  = DeviceFactory.createRadarGui();	
			Controller.activate(led, sonar, radar);
		}
	} 

	public static void main( String[] args) throws Exception {
		new RadarSystemMain().setup();
	}
}

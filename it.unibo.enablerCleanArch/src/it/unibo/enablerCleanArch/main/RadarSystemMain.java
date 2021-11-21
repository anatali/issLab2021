package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.adapters.LedAdapterClient;
import it.unibo.enablerCleanArch.adapters.RadarGuiAdapter;
import it.unibo.enablerCleanArch.adapters.SonarAdapterServer;
import it.unibo.enablerCleanArch.domain.*;
 
public class RadarSystemMain {

	public void setup() throws Exception {
		RadarSystemConfig.setTheConfiguration(   );
		//Input
		ISonar sonar     = RadarSystemConfig.SonareRemote    ? new SonarAdapterServer("sonarAdapterServer") : DeviceFactory.createSonar();

		//Output
		ILed led         = RadarSystemConfig.LedRemote       ? new LedAdapterClient( ) : DeviceFactory.createLed();
		IRadarGui radar  = RadarSystemConfig.RadarGuieRemote ? new RadarGuiAdapter(  ) : DeviceFactory.createRadarGui();
		
		//Control
		Controller.activate(led, sonar, radar);
	} 

	public static void main( String[] args) throws Exception {
		new RadarSystemMain().setup();
	}
}

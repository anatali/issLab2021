package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.adapters.LedAdapter;
import it.unibo.enablerCleanArch.adapters.RadarGuiAdapter;
import it.unibo.enablerCleanArch.adapters.SonarAdapter;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.useCases.LedAlarmUsecase;
import it.unibo.enablerCleanArch.useCases.RadarGuiUsecase;
 

public class RadarSystemMain {
 

	public void setup() throws Exception {
 		
		//Input
		ISonar sonar     = RadarSystemConfig.SonareRemote    ? new SonarAdapter()      : DeviceFactory.createSonar();

		//Output
		ILed led         = RadarSystemConfig.ledRemote       ? new LedAdapter( )       : DeviceFactory.createLed();
		IRadarGui radar  = RadarSystemConfig.RadarGuieRemote ? new RadarGuiAdapter(  ) : DeviceFactory.createRadarGui();
		
		//Control
		Controller.activate(led, sonar, radar);
	} 

	public static void main( String[] args) throws Exception {
		new RadarSystemMain().setup();
	}
}

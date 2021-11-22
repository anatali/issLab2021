package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.adapters.LedAdapterClient;
import it.unibo.enablerCleanArch.adapters.RadarGuiAdapterServer;
import it.unibo.enablerCleanArch.adapters.SonarAdapterServer;
import it.unibo.enablerCleanArch.domain.*;

 
public class RadarSystemMain {
private ISonar sonar = null;

	public void setup() throws Exception {
		RadarSystemConfig.setTheConfiguration(   );
		
		//Control
		if( RadarSystemConfig.ControllerRemote ) {
			DeviceFactory.createRadarGui();			
			new RadarGuiAdapterServer( RadarSystemConfig.radarGuiPort );
		}else {
			//Input
			sonar            = RadarSystemConfig.SonareRemote   ? new SonarAdapterServer("sonarAdapterServer") : DeviceFactory.createSonar();
			//Output
			ILed led         = RadarSystemConfig.LedRemote       ? new LedAdapterClient( ) : DeviceFactory.createLed();
			//IRadarGui radar  = RadarSystemConfig.RadarGuieRemote ? new RadarGuiAdapterClient(  ) : DeviceFactory.createRadarGui();
			IRadarGui radar  = DeviceFactory.createRadarGui();	
			Controller.activate(led, sonar, radar);
  		}
	} 
	
	public void dowork() {
		if( sonar != null ) sonar.activate();
	}

	public static void main( String[] args) throws Exception {
		RadarSystemMain sys = new RadarSystemMain();
		sys.setup();
		sys.dowork();
	}
}

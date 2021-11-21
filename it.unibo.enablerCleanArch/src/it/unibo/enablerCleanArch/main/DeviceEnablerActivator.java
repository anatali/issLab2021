package it.unibo.enablerCleanArch.main;
import it.unibo.enablerCleanArch.enablers.*;

/*
 * Main program for the RapberryPi and real devices
 * TODO: creare un file di configurazione o di properties
 */

public class DeviceEnablerActivator {
  	
	public static void main( String[] args) throws Exception {
        RadarSystemConfig.setTheConfiguration(   );
        
 		if( RadarSystemConfig.LedRemote ) {
			new LedServer(  RadarSystemConfig.ledPort );
		}
 		
		if( RadarSystemConfig.SonareRemote  ) {
	 		Thread.sleep(RadarSystemConfig.applStartdelay);  //Give time to start the application  on the PC
			new SonarClient( RadarSystemConfig.pcHostAddr, RadarSystemConfig.sonarPort );
		}
	}

}

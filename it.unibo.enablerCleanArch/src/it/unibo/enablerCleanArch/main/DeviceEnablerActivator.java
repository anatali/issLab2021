package it.unibo.enablerCleanArch.main;
import it.unibo.enablerCleanArch.enablers.*;

/*
 * Main program for the RapberryPi and real devices
 */

public class DeviceEnablerActivator {
 	
 	public static void main( String[] args) throws Exception {
		if( RadarSystemConfig.ledRemote ) {
			new LedServer(  RadarSystemConfig.ledPort );
		}
		if( RadarSystemConfig.SonareRemote  ) {
			new SonarClient( RadarSystemConfig.pcHostAddr, RadarSystemConfig.sonarPort );
		}
	}

}

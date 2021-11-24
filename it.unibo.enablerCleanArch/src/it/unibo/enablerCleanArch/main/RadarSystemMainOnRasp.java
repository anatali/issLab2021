package it.unibo.enablerCleanArch.main;
import it.unibo.enablerCleanArch.domain.Controller;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.IRadarGui;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.*;

/*
 * Main program for the RapberryPi and real devices
 * TODO: creare un file di configurazione o di properties
 */

public class RadarSystemMainOnRasp {
  	
	public static void main( String[] args) throws Exception {
 		RadarSystemConfig.setTheConfiguration(   );
				
		ISonar sonar    = DeviceFactory.createSonar();
		ILed   led      = DeviceFactory.createLed();
				
		if( RadarSystemConfig.ControllerRemote ) {  //Controller on PC
 			new LedServer( "LedServer", RadarSystemConfig.ledPort );
			Thread.sleep(RadarSystemConfig.applStartdelay);  //Give time to start the application  on the PC
			new SonarClient( "SonarClient", RadarSystemConfig.pcHostAddr, RadarSystemConfig.sonarPort );
			new RadarGuiClient( "RadarGuiClient", RadarSystemConfig.pcHostAddr, RadarSystemConfig.radarGuiPort ); 
		}else { //Controller on Rasp
			System.out.println("Controller on Rasp sonar=" + sonar);
 			IRadarGui radar =  new RadarGuiClient( "RadarGuiClient", RadarSystemConfig.pcHostAddr, RadarSystemConfig.radarGuiPort ); 
 			//Control
			Controller.activate( led, sonar, radar );
 		}
		if( sonar != null ) sonar.activate();
 		
	}

}

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

public class DeviceEnablerActivator {
  	
	public static void main( String[] args) throws Exception {
 		RadarSystemConfig.setTheConfiguration(   );
				
		ISonar sonar = DeviceFactory.createSonar();
		ILed   led   = DeviceFactory.createLed();
		
		
		if( RadarSystemConfig.ControllerRemote ) {  //Controller on PC
 			new LedServer(  RadarSystemConfig.ledPort );
			Thread.sleep(RadarSystemConfig.applStartdelay);  //Give time to start the application  on the PC
			new SonarClient( RadarSystemConfig.pcHostAddr, RadarSystemConfig.sonarPort );
		}else { //Controller on Rasp
			System.out.println("Controller on Rasp");
 			IRadarGui radar =  new RadarGuiClient(  ); 
			//Control
			Controller.activate( led, sonar, radar );
			
		}
 		
		
	}

}

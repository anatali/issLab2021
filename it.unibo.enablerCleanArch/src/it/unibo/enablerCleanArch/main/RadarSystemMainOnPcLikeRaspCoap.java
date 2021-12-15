package it.unibo.enablerCleanArch.main;
import it.unibo.enablerCleanArch.domain.Controller;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.IRadarDisplay;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.*;
import it.unibo.enablerCleanArch.supports.coap.CoapLedResource;
import it.unibo.enablerCleanArch.supports.coap.CoapSonarResource;
import it.unibo.enablerCleanArch.supports.coap.DeviceType;

/*
 * Main program for the RapberryPi and real devices
 * TODO: creare un file di configurazione o di properties
 */

public class RadarSystemMainOnPcLikeRaspCoap {
  	
	public static void main( String[] args) throws Exception {
 		RadarSystemConfig.setTheConfiguration( "RadarSystemConfigPcSimulatedDevices.json"  );
 				
		if( RadarSystemConfig.ControllerRemote ) {  //Controller on PC
 			 new CoapLedResource("led", DeviceType.input);
 			 //Thread.sleep(RadarSystemConfig.applStartdelay);  //Give time to start the application  on the PC
			 new CoapSonarResource("sonar",DeviceType.output) ;    			
		}else { //Controller on Rasp
			System.out.println("Controller on PcLikeRasp  "  );
			ISonar sonar    = DeviceFactory.createSonar();
			ILed   led      = DeviceFactory.createLed();
 			IRadarDisplay radar =  
 					new RadarGuiClient( "RadarGuiClient", RadarSystemConfig.pcHostAddr, ""+RadarSystemConfig.radarGuiPort, ProtocolType.tcp ); 
 			//Control
			Controller.activate( led, sonar, radar );
 		}
 		
	}

}

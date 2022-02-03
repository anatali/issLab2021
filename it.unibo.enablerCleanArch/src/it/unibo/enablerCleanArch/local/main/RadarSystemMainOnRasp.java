package it.unibo.enablerCleanArch.local.main;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;

/*
 * Applicazione che va in coppia con RadarSystemDevicesOnRasp
 */

public class RadarSystemMainOnRasp implements IApplication{
private IRadarDisplay radar;
private ISonar sonar;
private ILed  led ;
private Controller controller;

	@Override
	public String getName() {	 
		return "RadarSystemMainOnRasp";
	}

	public void setup( String configFile )  {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
		else {
			RadarSystemConfig.simulation   		= false;
  			RadarSystemConfig.testing      		= false;			
			RadarSystemConfig.DLIMIT      		= 12;  
			RadarSystemConfig.sonarDelay        = 250;
		}
 	}
	
 	
	@Override
	public void doJob(String configFileName) {
		setup(configFileName);
		configure();
		controller.start();
	}
	
	protected void configure() {
		//Dispositivi di Input
	    sonar      = DeviceFactory.createSonar();
	    //Dispositivi di Output
	    led        = DeviceFactory.createLed();
	    radar      = null;
	    //Controller
	    ActionFunction endFun = (n) -> { System.out.println(n); terminate(); };
	    controller = Controller.create(led, sonar, radar, endFun);	 
	}
  
	public void terminate() {
		led.turnOff();
		//System.exit(0);
	}

 
 	public IRadarDisplay getRadarGui() {
		return radar;
	}

	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainOnRasp().doJob("RadarSystemConfig.json");
 	}

}

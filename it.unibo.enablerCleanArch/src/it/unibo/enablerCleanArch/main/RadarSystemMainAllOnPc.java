package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.domain.*;

public class RadarSystemMainAllOnPc implements IApplication{
	
	@Override
	public String getName() {	 
		return "RadarSystemMainAllOnPc";
	}

	public void setup( String configFile )  {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
		else {
			RadarSystemConfig.simulation   		= true;
			RadarSystemConfig.ledGui            = true;
 			RadarSystemConfig.testing      		= false;			
			RadarSystemConfig.DLIMIT      		= 40; //12 su Rasp
			RadarSystemConfig.sonarDelay        = 250;
			RadarSystemConfig.RadarGuiRemote    = false;
		}
 	}
	
 	
	@Override
	public void doJob(String configFileName) {
		setup(configFileName);
		executeAllOnPc();
	}
	
	public void executeAllOnPc() {
		IRadarDisplay radar;
		//Dispositivi di Input
	    ISonar sonar         = DeviceFactory.createSonar();
	    //Dispositivi di Output
	    ILed  led            = DeviceFactory.createLed();
	    if( ! RadarSystemConfig.RadarGuiRemote ) {
	    	radar  = DeviceFactory.createRadarGui();
	    }else {
	    	radar = null;
	    }
	    //Controller
	    Controller.activate(led, sonar, radar);	//activates the sonar
	}
	
 
	public void terminate() {
		System.exit(0);
	}

 
 	public IRadarDisplay getRadarGui() {
		return radar;
	}

	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainAllOnPc().doJob(null);
 	}

}

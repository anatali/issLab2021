package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.domain.*;

public class RadarSystemMainOnPc {
private ISonar sonar        = null;
private ILed led            = null;
private IRadarDisplay radar = null;

	public void setup( String configFile ) throws Exception {
		RadarSystemConfig.setTheConfiguration( configFile );
	}
	
	public void build() throws Exception {			
		//Dispositivi di Input
		sonar  = DeviceFactory.createSonar();
		//Dispositivi di Output
		led    = DeviceFactory.createLed();
		radar  = DeviceFactory.createRadarGui();	
		//Controller 
		Controller.activate(led, sonar, radar);
	} 
	
	public void activateSonar() {
		if( sonar != null ) sonar.activate();
	}

	//ADDED for testing
	//-------------------------------------------------
	public ILed getLed() {
		return led;
	}
	public ISonar getSonar() {
		return sonar;
	}
	public IRadarDisplay getRadarGui() {
		return radar;
	}

	
	public static void main( String[] args) throws Exception {
		RadarSystemMainOnPc sys = new RadarSystemMainOnPc();
		sys.setup("RadarSystemConfigAllOnPc.json");
		sys.build();
		sys.activateSonar();
	}
}

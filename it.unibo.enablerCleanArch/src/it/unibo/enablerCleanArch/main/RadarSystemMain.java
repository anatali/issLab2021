package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.adapters.LedAdapter;
import it.unibo.enablerCleanArch.adapters.RadarAdapter;
import it.unibo.enablerCleanArch.adapters.SonarAdapter;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.useCases.LedAlarmUsecase;
import it.unibo.enablerCleanArch.useCases.RadarGuiUsecase;
 

public class RadarSystemMain {
 

	public void setup() throws Exception {
		ILed led            = DeviceFactory.createLed();
		ISonar sonar        = DeviceFactory.createSonar();
		
		ILed ledAdapter     = new LedAdapter( led );
		ISonar sonarAdapter = new SonarAdapter( sonar );
		
		IRadar radarAdapter = new RadarAdapter();
		 
		Controller.activate(ledAdapter, sonarAdapter, radarAdapter);
	} 

	public static void main( String[] args) throws Exception {
		new RadarSystemMain().setup();
	}
}

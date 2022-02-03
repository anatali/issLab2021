package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.supports.ColorsOut;

/*
 * Crea il Sonar come POJO (observable)
 */
public abstract class SonarUsageAbstractMain {
	protected ISonar   sonar;

	public void setUp(String fName) {
		if( fName != null ) RadarSystemConfig.setTheConfiguration(fName);
		else {
			RadarSystemConfig.pcHostAddr         = "localhost";
			RadarSystemConfig.sonarDelay         = 100;		
			RadarSystemConfig.sonarObservable    = true;	
		}		
	}
 	
	public void configure() {
  		createTheSonar();
 		createObservers();
 		configureTheServer();
 		ColorsOut.outappl("SonarUsageMainCoap | configure done", ColorsOut.ANSI_PURPLE  );
	}

	protected void createTheSonar() {
		sonar = DeviceFactory.createSonar(RadarSystemConfig.sonarObservable);		
	}
	
 	protected abstract void configureTheServer();
	protected  abstract void createObservers(); 
	public abstract void execute();

	
}

package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.ISonarObservable;
import it.unibo.enablerCleanArch.domain.SonarObserverFortesting;
import it.unibo.enablerCleanArch.supports.Colors;

/*
 * Crea il Sonar come POJO (observable)
 */
public abstract class SonarUsageAbstractMain {
	protected ISonar   sonar;
 	protected IObserver obsfortesting;

 
	public void setConfiguration() {
		RadarSystemConfig.pcHostAddr         = "localhost";
		RadarSystemConfig.sonarDelay         = 100;		
		RadarSystemConfig.sonarObservable    = true;		
	}
	
	public void configure() {
		setConfiguration();
 		createTheSonar();
 		configureTheServer();
 		Colors.outappl("SonarUsageMainCoap | configure done", Colors.ANSI_PURPLE  );
	}

	protected void createTheSonar() {
		sonar = DeviceFactory.createSonar(RadarSystemConfig.sonarObservable);
		if( RadarSystemConfig.sonarObservable ) {
			boolean oneShot         = false;
			obsfortesting           = new SonarObserverFortesting("obsfortesting", oneShot) ;
	 		sonar                   = DeviceFactory.createSonarObservable();
			((ISonarObservable) sonar).register( obsfortesting );
		} 		
	}
	
 	protected abstract void configureTheServer();
	
	public abstract void execute();

	
}

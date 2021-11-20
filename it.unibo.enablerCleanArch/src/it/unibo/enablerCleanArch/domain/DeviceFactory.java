package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class DeviceFactory {

	public static ILed createLed() {
		if( RadarSystemConfig.simulation)  {
			return LedMock.create();
		}else {
			return LedConcrete.create();
		}
	}
	
	public static ISonar createSonar() {
		if( RadarSystemConfig.simulation)  {
			return SonarMock.create();
		}else { 
			return SonarConcrete.create();
		}
		
	}
	
}

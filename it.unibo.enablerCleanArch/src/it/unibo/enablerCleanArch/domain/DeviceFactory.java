package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class DeviceFactory {

	public static ILed createLed() {
		System.out.println("DeviceFactory | createLed simulated="+RadarSystemConfig.simulation);
		if( RadarSystemConfig.simulation)  {
			return LedAbstract.createLedMock();
		}else {
			return LedAbstract.createLedConcrete();
		}
	}
	
	public static ISonar createSonar() {
		System.out.println("DeviceFactory | createSonar simulated="+RadarSystemConfig.simulation);
		if( RadarSystemConfig.simulation)  {
			return SonarBuilder.createSonarMock();
		}else { 
			return SonarBuilder.createSonarConcrete();
		}
	}
	
	//We do not have mock for RadarGui
	public static IRadarGui createRadarGui() {
		return RadarGui.create();
	}
	
}

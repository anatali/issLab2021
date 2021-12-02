package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class DeviceFactory {

	public static ILed createLed() {
		System.out.println("DeviceFactory | createLed simulated="+RadarSystemConfig.simulation);
		if( RadarSystemConfig.simulation)  {
			return LedModel.createLedMock();
		}else {
			return LedModel.createLedConcrete();
		}
	}
	
	public static ISonar createSonar() {
		System.out.println("DeviceFactory | createSonar simulated="+RadarSystemConfig.simulation);
		if( RadarSystemConfig.simulation)  {
			return SonarModel.createSonarMock();
		}else { 
			return SonarModel.createSonarConcrete();
		}
	}
	
	//We do not have mock for RadarGui
	public static IRadarDisplay createRadarGui() {
		return RadarGui.create();
	}
	
}

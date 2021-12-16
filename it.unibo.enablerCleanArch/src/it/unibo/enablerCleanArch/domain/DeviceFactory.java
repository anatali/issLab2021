package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;

public class DeviceFactory {

	public static ILed createLed() {
		//Colors.out("DeviceFactory | createLed simulated="+RadarSystemConfig.simulation);
		if( RadarSystemConfig.simulation)  {
			return LedModel.createLedMock();
		}else {
			return LedModel.createLedConcrete();
		}
	}
	
	public static ISonar createSonar() {
		//Colors.out("DeviceFactory | createSonar simulated="+RadarSystemConfig.simulation);
		if( RadarSystemConfig.simulation)  {
			return SonarModel.createSonarMock();
		}else { 
			return SonarModel.createSonarConcrete();
		}
	}
	public static ISonarObservable createSonarObservable() {
		Colors.out("DeviceFactory | createSonarObservable simulated="+RadarSystemConfig.simulation);
		if( RadarSystemConfig.simulation)  {
			return new SonarMockObservable();
		}else { 
			return null;
		}
		
	}
	
	//We do not have mock for RadarGui
	public static IRadarDisplay createRadarGui() {
		return RadarDisplay.getRadarDisplay();
	}
	
}

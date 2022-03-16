package it.unibo.radarSystem22.actors.domain.support;

import it.unibo.kactor.ActorBasic;
import it.unibo.radarSystem22.actors.domain.LedMockActor;
import it.unibo.radarSystem22.actors.domain.RadarDisplayActor;
import it.unibo.radarSystem22.actors.domain.SonarMockActor;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.domain.interfaces.*;


public class DeviceActorFactory {

	public static ActorBasic createLed(String name) {
		//Colors.out("DeviceFactory | createLed simulated="+RadarSystemConfig.simulation);
		if( DomainSystemConfig.simulation)  {
			 return new LedMockActor(name);
		}else {
			return null;
		}
	}
	public static void createSonar(String name, boolean observable) {
		if( observable )  createSonarObservable(name);
		else  createSonar(name);
	}

	public static ActorBasic createSonar(String name) {
		//Colors.out("DeviceFactory | createSonar simulated="+RadarSystemConfig.simulation);
		if( DomainSystemConfig.simulation)  {
			return new SonarMockActor(name);
		}else { 
			return null;
		}
	}
	public static void createSonarObservable(String name) {
		ColorsOut.out("DeviceFactory | createSonarObservable simulated="+DomainSystemConfig.simulation);
		if( DomainSystemConfig.simulation)  {
			//return new SonarMockObservable();
		}else { 
			//return new SonarConcreteObservable();
		}	
	}
	
	//We do not have mock for RadarGui
	public static IRadarDisplay createRadarGui() {
		return new RadarDisplayActor("radarDisplay");
	}
	
}

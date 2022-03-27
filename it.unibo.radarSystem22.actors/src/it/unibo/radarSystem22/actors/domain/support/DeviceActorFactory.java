package it.unibo.radarSystem22.actors.domain.support;

import it.unibo.kactor.Actor22;
import it.unibo.radarSystem22.actors.domain.LedActor;
import it.unibo.radarSystem22.actors.domain.RadarActor;
import it.unibo.radarSystem22.actors.domain.SonarActor;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;



public class DeviceActorFactory {

	public static Actor22 createLed(String name) {
		//Colors.out("DeviceFactory | createLed simulated="+RadarSystemConfig.simulation);
		if( DomainSystemConfig.simulation)  {
			 return new LedActor(name);
		}else {
			return null;
		}
	}
	public static void createSonar(String name, boolean observable) {
		if( observable )  createSonarObservable(name);
		else  createSonar(name);
	}

	public static Actor22 createSonar(String name) {
		//Colors.out("DeviceFactory | createSonar simulated="+RadarSystemConfig.simulation);
		if( DomainSystemConfig.simulation)  {
			return new SonarActor(name);
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
	public static Actor22 createRadarActor() {
		return new RadarActor("radar");
	}
	
}

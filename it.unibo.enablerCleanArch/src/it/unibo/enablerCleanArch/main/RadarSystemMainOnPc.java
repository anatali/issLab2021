package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.adapters.LedAdapterClient;
import it.unibo.enablerCleanArch.adapters.RadarGuiAdapterServer;
import it.unibo.enablerCleanArch.adapters.SonarAdapterServer;
import it.unibo.enablerCleanArch.domain.*;

 
public class RadarSystemMainOnPc {
private ISonar sonar    = null;
private ILed led        = null;
private IRadarGui radar = null;

	public void setup( String configFile ) throws Exception {
		RadarSystemConfig.setTheConfiguration( configFile );
	}
	public void setup() throws Exception {			
		//RadarSystemConfig.setTheConfiguration( "RadarSystemConfigPcControllerAndGui.json"  );   
		//Control
		if( RadarSystemConfig.ControllerRemote ) {
			radar =  DeviceFactory.createRadarGui();			
			new RadarGuiAdapterServer( RadarSystemConfig.radarGuiPort );
		}else { //Controller locale (al PC)
			//Input
			sonar  = RadarSystemConfig.SonareRemote   
					? new SonarAdapterServer("sonarAdapterServer", RadarSystemConfig.sonarPort) 
					: DeviceFactory.createSonar();
			//Output
			led    = RadarSystemConfig.LedRemote   
					? new LedAdapterClient( "LedAdapterClient", RadarSystemConfig.raspHostAddr, RadarSystemConfig.ledPort  ) 
					: DeviceFactory.createLed();
			radar  = DeviceFactory.createRadarGui();	
			Controller.activate(led, sonar, radar);
  		}
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

	public IRadarGui getRadarGui() {
		return radar;
	}
	/*
	//La TestUnit decide di attivare il sistema
	public void oneShotSonarForTesting( int distance ) {
		if( sonar != null ) {
			SonarMock sonarForTesting = (SonarMock) sonar;
			sonarForTesting.oneShot( distance );
		}
	}*/
	
	public static void main( String[] args) throws Exception {
		RadarSystemMainOnPc sys = new RadarSystemMainOnPc();
		sys.setup();
		sys.activateSonar();
	}
}

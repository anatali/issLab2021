package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.adapters.LedAdapterClient;
import it.unibo.enablerCleanArch.adapters.RadarGuiAdapterServer;
import it.unibo.enablerCleanArch.adapters.SonarAdapterEnablerAsServer;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.ProtocolType;

 
public class RadarSystemAllOnPc {
private ISonar sonar        = null;
private ILed led            = null;
private IRadarDisplay radar = null;

	public void setup( String configFile ) throws Exception {
		RadarSystemConfig.setTheConfiguration( configFile );
	}
	public void build() throws Exception {			
		//RadarSystemConfig.setTheConfiguration( "RadarSystemConfigPcControllerAndGui.json"  );   
		//Control
		if( RadarSystemConfig.ControllerRemote ) {
			radar =  DeviceFactory.createRadarGui();			
			new RadarGuiAdapterServer( RadarSystemConfig.radarGuiPort );
		}else { //Controller locale (al PC)
			//Input
			sonar  = RadarSystemConfig.SonareRemote   
					? new SonarAdapterEnablerAsServer("sonarAdapterServer",  RadarSystemConfig.sonarPort, ProtocolType.tcp)   
					: DeviceFactory.createSonar();
			//Output
			led    = RadarSystemConfig.LedRemote   
					? new LedAdapterClient( "LedAdapterClient", RadarSystemConfig.raspHostAddr, RadarSystemConfig.ledPort, ProtocolType.tcp  ) 
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

	public IRadarDisplay getRadarGui() {
		return radar;
	}

	
	public static void main( String[] args) throws Exception {
		RadarSystemAllOnPc sys = new RadarSystemAllOnPc();
		sys.setup("RadarSystemConfigAllOnPc.json");
		sys.build();
		sys.activateSonar();
	}
}

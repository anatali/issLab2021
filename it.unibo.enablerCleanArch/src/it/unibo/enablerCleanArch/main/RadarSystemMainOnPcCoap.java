package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.adapters.LedAdapterClient;
import it.unibo.enablerCleanArch.adapters.LedAdapterCoap;
import it.unibo.enablerCleanArch.adapters.RadarGuiAdapterServer;
import it.unibo.enablerCleanArch.adapters.SonarAdapterCoap;
import it.unibo.enablerCleanArch.adapters.SonarAdapterServer;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;

 
public class RadarSystemMainOnPcCoap {
private ISonar sonar    = null;
private ILed led        = null;
private IRadarGui radar = null;

	public void setup() throws Exception {			
		RadarSystemConfig.setTheConfiguration( "RadarSystemConfigPcControllerAndGui.json"  );   
		//Control
		if( RadarSystemConfig.ControllerRemote ) {
			radar =  DeviceFactory.createRadarGui();			
			new RadarGuiAdapterServer( RadarSystemConfig.radarGuiPort );
		}else { //Controller locale (al PC)
			//Input
			sonar  = RadarSystemConfig.SonareRemote   
					? new SonarAdapterCoap("localhost", CoapApplServer.inputDeviceUri+"/sonar") 	//:5683 lo sa CoapSupport
					: DeviceFactory.createSonar();
			//Output
			led    = RadarSystemConfig.LedRemote   
					? new LedAdapterCoap( "localhost", CoapApplServer.outputDeviceUri+"/led" ) 
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
	
	//La TestUnit decide di attivare il sistema
	public void oneShotSonarForTesting( int distance ) {
		if( sonar != null ) {
			SonarMock sonarForTesting = (SonarMock) sonar;
			sonarForTesting.oneShot( distance );
		}
	}
	
	public static void main( String[] args) throws Exception {
		RadarSystemMainOnPcCoap sys = new RadarSystemMainOnPcCoap();
		sys.setup();
		sys.activateSonar();
	}
}

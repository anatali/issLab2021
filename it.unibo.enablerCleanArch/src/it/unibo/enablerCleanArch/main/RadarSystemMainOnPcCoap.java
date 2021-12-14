package it.unibo.enablerCleanArch.main;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.LedAdapterCoap;
import it.unibo.enablerCleanArch.supports.coap.SonarAdapterCoap;
import it.unibo.enablerCleanArch.supports.coap.SonarAdapterCoapObserver;
 
public class RadarSystemMainOnPcCoap {
private ISonar sonar    = null;
private ILed led        = null;
private IRadarDisplay radar = null;

	public void setup() throws Exception {			
		//RadarSystemConfig.setTheConfiguration( "RadarSystemConfigPcControllerAndGui.json"  );   
		//Control 
		//TODO ???
		/*
		if( RadarSystemConfig.ControllerRemote ) {
			radar =  DeviceFactory.createRadarGui();			
			new RadarGuiAdapterServer( RadarSystemConfig.radarGuiPort );
		}else { //Controller locale (al PC)
			//Input
			sonar  = RadarSystemConfig.SonareRemote   
					? new SonarAdapterCoapObserver("localhost", CoapApplServer.inputDeviceUri+"/sonar") 	//:5683 lo sa CoapSupport
					: DeviceFactory.createSonar();
			//Output
			led    = RadarSystemConfig.LedRemote   
					? new LedAdapterCoap( "localhost", CoapApplServer.outputDeviceUri+"/led" ) 
					: DeviceFactory.createLed();
			radar  = DeviceFactory.createRadarGui();	
			Controller.activate(led, sonar, radar);
  		}*/
		
		RadarSystemConfig.protcolType = ProtocolType.coap;
		//Controller locale (al PC)
		//sonar = new SonarAdapterCoapObserver("localhost", CoapApplServer.inputDeviceUri+"/sonar");
		sonar = new SonarAdapterCoap("localhost", CoapApplServer.inputDeviceUri+"/sonar");
		led   = new LedAdapterCoap( "localhost", CoapApplServer.outputDeviceUri+"/led" ) ;
		
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
	/*
	//La TestUnit decide di attivare il sistema
	public void oneShotSonarForTesting( int distance ) {
		if( sonar != null ) {
			SonarMock sonarForTesting = (SonarMock) sonar;
			sonarForTesting.oneShot( distance );
		}
	}
	*/
	public static void main( String[] args) throws Exception {
		RadarSystemMainOnPcCoap sys = new RadarSystemMainOnPcCoap();
		sys.setup();
		sys.activateSonar();
	}
}

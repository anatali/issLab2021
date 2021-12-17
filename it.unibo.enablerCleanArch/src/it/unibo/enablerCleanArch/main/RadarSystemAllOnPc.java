package it.unibo.enablerCleanArch.main;


import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.devices.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.devices.LedApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.SonarApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;

 
public class RadarSystemAllOnPc {
private ISonar sonar          = null;
private ILed led              = null;
private IRadarDisplay radar   = null;
 
	public void setup( String configFile ) throws Exception {
		RadarSystemConfig.setTheConfiguration( configFile );
	}
	
	protected ISonar simulateSonarRemote(ProtocolType protocol) {
		sonar = SonarModel.create();
		
		EnablerAsServer sonarServer  = 
		new EnablerAsServer("sonarServer",RadarSystemConfig.sonarPort, protocol, new SonarApplHandler("sonarH",sonar) );
		sonar.activate();
		sonarServer.activate();
		
		String nameUri = CoapApplServer.outputDeviceUri+"/sonar";
		String entry   = protocol==ProtocolType.coap ? nameUri : ""+RadarSystemConfig.sonarPort;
		ISonar sonarClient = new SonarProxyAsClient("sonarClient", "localhost",entry, protocol );
		return sonarClient;
	}
	protected ILed simulateLedRemote(ProtocolType protocol) {
		led = LedModel.create();
		EnablerAsServer ledServer  = 
				new EnablerAsServer("LedEnablerAsServer",RadarSystemConfig.ledPort, 
				protocol,  new LedApplHandler("ledH", led) );
		ledServer.activate();
		String nameUri = CoapApplServer.inputDeviceUri+"/led";
		String entry   = protocol==ProtocolType.coap ? nameUri : ""+RadarSystemConfig.sonarPort;
		ILed ledClient = new LedProxyAsClient("ledClient", "localhost",entry, protocol);
		return ledClient;
		
	}
	public void buildAndRun(ProtocolType protocol) throws Exception {			
			//Controller locale (al PC)
			//Input
			sonar  = RadarSystemConfig.SonareRemote ? simulateSonarRemote(protocol) :  SonarModel.create() ;
			//Output
			led    = RadarSystemConfig.LedRemote ? simulateLedRemote(protocol) : LedModel.create();
			radar  = DeviceFactory.createRadarGui();	
			
			
			Controller.activate(led, sonar, radar); 		

			//if( sonar != null ) sonar.activate();

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
 

	
	public static void main( String[] args) throws Exception {
		RadarSystemAllOnPc sys = new RadarSystemAllOnPc();
		sys.setup("RadarSystemConfigAllOnPc.json");
		RadarSystemConfig.simulation   = true;
		RadarSystemConfig.SonareRemote = true;
		RadarSystemConfig.LedRemote    = true;
		RadarSystemConfig.sonarPort    = 8012;
		RadarSystemConfig.ledPort      = 8010;
		RadarSystemConfig.sonarDelay = 100;
		RadarSystemConfig.testing    = false;
		sys.buildAndRun(ProtocolType.tcp);
		//sys.activateSonar();
	}
}

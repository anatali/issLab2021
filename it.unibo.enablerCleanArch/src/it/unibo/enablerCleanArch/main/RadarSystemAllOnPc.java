package it.unibo.enablerCleanArch.main;


import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.devices.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.devices.LedApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.SonarApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.SonarProxyAsClient;

 
public class RadarSystemAllOnPc {
private ISonar sonar          = null;
private ILed led              = null;
private IRadarDisplay radar   = null;
private ProtocolType protocol = ProtocolType.tcp;	//could be overwritten

	public void setup( String configFile ) throws Exception {
		RadarSystemConfig.setTheConfiguration( configFile );
	}
	
	protected ISonar simulateSonarRemote() {
		//EnablerAsServer sonarServer  = 
		new EnablerAsServer("sonarServer",RadarSystemConfig.sonarPort, protocol, new SonarApplHandler("sonarH") );
		ISonar sonarClient = 
				new SonarProxyAsClient("sonarClient", "localhost",""+RadarSystemConfig.sonarPort, protocol );
		return sonarClient;
	}
	protected ILed simulateLedRemote() {
		new EnablerAsServer("LedEnablerAsServer",RadarSystemConfig.ledPort, 
				protocol,  new LedApplHandler("ledH") );
		ILed ledClient = new LedProxyAsClient(
				"ledClient", "localhost",""+RadarSystemConfig.ledPort, protocol);
		return ledClient;
		
	}
	public void build() throws Exception {			
			//Controller locale (al PC)
			//Input
			sonar  = RadarSystemConfig.SonareRemote ? simulateSonarRemote() : DeviceFactory.createSonar();
			//Output
			led    = RadarSystemConfig.LedRemote ? simulateLedRemote() : DeviceFactory.createLed();
			radar  = DeviceFactory.createRadarGui();	
			Controller.activate(led, sonar, radar); 		
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
		sys.build();
		sys.activateSonar();
	}
}

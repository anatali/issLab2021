package it.unibo.enablerCleanArch.main;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.devices.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.devices.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.LedResourceCoap;
import it.unibo.enablerCleanArch.supports.coap.SonarResourceCoap;

 
public class RadarSystemAllOnPcCoap {
private ISonar sonar          = null;
private ILed led              = null;
private IRadarDisplay radar   = null;
private ProtocolType protocol = ProtocolType.coap;	//could be overwritten

	public void setup( String configFile ) throws Exception {
		RadarSystemConfig.setTheConfiguration( configFile );
		RadarSystemConfig.SonareRemote = true;
		RadarSystemConfig.LedRemote    = true;		 
	}
	
	protected ISonar simulateSonarRemote() {
		sonar          = DeviceFactory.createSonar();
		SonarResourceCoap sonarResource = new SonarResourceCoap("sonar", sonar);
		//new EnablerAsServer("SonarCoap",RadarSystemConfig.sonarPort, protocol, sonarResource );
		String sonarUri    = CoapApplServer.inputDeviceUri+"/"+sonarResource.getName();
		ISonar sonarClient = new SonarProxyAsClient(sonarUri, "localhost",""+RadarSystemConfig.sonarPort, protocol );
		return sonarClient;
	}
	protected ILed simulateLedRemote() {
		LedResourceCoap ledResource =  new LedResourceCoap("led", led);
		String ledUri  = CoapApplServer.outputDeviceUri+"/"+ledResource.getName();
		ILed ledClient = new LedProxyAsClient(ledUri, "localhost",""+RadarSystemConfig.ledPort, protocol);
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

	public IRadarDisplay getRadarGui() {
		return radar;
	}

	
	public static void main( String[] args) throws Exception {
		RadarSystemAllOnPcCoap sys = new RadarSystemAllOnPcCoap();
		sys.setup("RadarSystemConfigAllOnPc.json");
		sys.build();
		sys.activateSonar();
	}
}

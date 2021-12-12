package it.unibo.enablerCleanArch.main;


import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.devices.LedAdapterEnablerAsClient;
import it.unibo.enablerCleanArch.enablers.devices.LedApplHandlerCoap;
import it.unibo.enablerCleanArch.enablers.devices.SonarApplHandlerCoap;
import it.unibo.enablerCleanArch.enablers.devices.SonarEnablerAsClient;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;

 
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
		SonarApplHandlerCoap sonarResource = new SonarApplHandlerCoap("sonar");
		//EnablerAsServer sonarServer  = 
				new EnablerAsServer("SonarCoap",RadarSystemConfig.sonarPort, protocol, sonarResource );
		String sonarUri = CoapApplServer.inputDeviceUri+"/"+sonarResource.getName();
		ISonar sonarClient = 
				new SonarEnablerAsClient(sonarUri, "localhost",RadarSystemConfig.sonarPort, protocol );
		return sonarClient;
	}
	protected ILed simulateLedRemote() {
		LedApplHandlerCoap ledResource =  new LedApplHandlerCoap("led");
		new EnablerAsServer("LedCoap",RadarSystemConfig.ledPort, protocol,  ledResource);
		String ledUri = CoapApplServer.outputDeviceUri+"/"+ledResource.getName();
		ILed ledClient = new LedAdapterEnablerAsClient(ledUri, "localhost",RadarSystemConfig.ledPort, protocol);
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

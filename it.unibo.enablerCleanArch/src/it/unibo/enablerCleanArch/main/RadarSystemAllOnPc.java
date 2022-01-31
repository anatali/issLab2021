package it.unibo.enablerCleanArch.main;


import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;
import it.unibo.enablerCleanArchapplHandlers.SonarApplHandler;

 
public class RadarSystemAllOnPc implements IApplication{
private ISonar sonar          = null;
private ILed led              = null;
private IRadarDisplay radar   = null;
private ISonar sonarClient;

	public void setUp( String configFile )   {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
		else {
			RadarSystemConfig.simulation   = true;
			RadarSystemConfig.SonareRemote = true;
			RadarSystemConfig.LedRemote    = true;
			RadarSystemConfig.sonarPort    = 8012;
			RadarSystemConfig.ledPort      = 8010;
			RadarSystemConfig.sonarDelay = 100;
			RadarSystemConfig.testing    = false;			
		}
	}
	
	protected ISonar simulateSonarRemote(ProtocolType protocol) {
		ColorsOut.out("simulateSonarRemote", ColorsOut.ANSI_PURPLE);
		sonar = SonarModel.create();
		
		EnablerAsServer sonarServer  = 
				new EnablerAsServer("sonarServer",RadarSystemConfig.sonarPort, 
						protocol, new SonarApplHandler("sonarH",sonar) );
		sonarServer.start();
		
		String nameUri = CoapApplServer.inputDeviceUri+"/sonar";
		String entry   = protocol==ProtocolType.coap ? nameUri : ""+RadarSystemConfig.sonarPort;
		sonarClient    = new SonarProxyAsClient("sonarClient", RadarSystemConfig.raspHostAddr,entry, protocol );
		return sonarClient;
	}
	
	protected ILed simulateLedRemote(ProtocolType protocol) {
		ColorsOut.out("simulateLedRemote", ColorsOut.ANSI_PURPLE);
		led = LedModel.create();
		EnablerAsServer ledServer  = 
				new EnablerAsServer("LedEnablerAsServer",RadarSystemConfig.ledPort, 
				protocol,  new LedApplHandler("ledH", led) );
		ledServer.start();
		String nameUri = CoapApplServer.outputDeviceUri+"/led";
		String entry   = protocol==ProtocolType.coap ? nameUri : ""+RadarSystemConfig.ledPort;
		ILed ledClient = new LedProxyAsClient("ledClient", RadarSystemConfig.raspHostAddr,entry, protocol);
		return ledClient;
		
	}
 
	
	protected void configure() {
		sonar  = RadarSystemConfig.SonareRemote ? 
				simulateSonarRemote(RadarSystemConfig.protcolType) :  SonarModel.create() ;
 		led    = RadarSystemConfig.LedRemote ? 
 				simulateLedRemote(RadarSystemConfig.protcolType) : LedModel.create();
		radar  = DeviceFactory.createRadarGui();	
		//Utils.delay(2000);
 	}
	
	protected void execute() {
		sonarClient.activate();	//activate the sonar
		Utils.delay(1000);
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
 
	@Override
	public void doJob(String configFileName) {
 		setUp(configFileName);
		configure();
		execute();
		Utils.delay(RadarSystemConfig.sonarDelay*70);
 		terminate();		
	}

	@Override
	public String getName() {
		return "RadarSystemAllOnPc";
	}

	protected void terminate() {
		System.exit(0);
	}
	
	public static void main( String[] args) throws Exception {
		new RadarSystemAllOnPc().doJob("RadarSystemConfig.json");
//		sys.setup("RadarSystemConfigAllOnPc.json");
//		sys.buildAndRun(ProtocolType.tcp);
		//sys.activateSonar();
	}

}

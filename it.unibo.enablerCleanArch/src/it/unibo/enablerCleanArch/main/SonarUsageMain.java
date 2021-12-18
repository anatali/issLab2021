package it.unibo.enablerCleanArch.main;

 
 
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.SonarModel;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.devices.SonarApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.LedResourceCoap;
import it.unibo.enablerCleanArch.supports.coap.SonarResourceCoap;
  

public class SonarUsageMain  {

private EnablerAsServer sonarServer;
private ISonar client1, client2;

	public void configure() {
		RadarSystemConfig.simulation  = true;
 		RadarSystemConfig.testing     = false;
 		RadarSystemConfig.sonarPort   = 8011;
		RadarSystemConfig.sonarDelay  = 500;
		RadarSystemConfig.protcolType = ProtocolType.tcp;
 		RadarSystemConfig.pcHostAddr  = "localhost";
 		
 		configureTheSonarEnablerServer();
 		configureTheSonarProxyClients();
 		System.out.println("SonarUsageMain | configure done"  );
	}
	
	protected void configureTheSonarEnablerServer() {
		ISonar sonar = DeviceFactory.createSonar();
		if( RadarSystemConfig.protcolType == ProtocolType.tcp) {
			sonarServer  = new EnablerAsServer("sonarServer",RadarSystemConfig.sonarPort, 
					RadarSystemConfig.protcolType, new SonarApplHandler("sonarH",sonar) );		
		}else if( RadarSystemConfig.protcolType == ProtocolType.coap){		
			new SonarResourceCoap("sonar", sonar);
		}
	}
	protected void configureTheSonarProxyClients() {		 
		String host           = RadarSystemConfig.pcHostAddr;
		ProtocolType protocol = RadarSystemConfig.protcolType;
		String nameUri        = CoapApplServer.inputDeviceUri+"/sonar";
		String entry    = protocol==ProtocolType.coap ? nameUri : ""+RadarSystemConfig.sonarPort;
		client1         = new SonarProxyAsClient("client1", host, entry, protocol );
		client1         = new SonarProxyAsClient("client2", host, entry, protocol );	
	}
	
	public void execute() {
		if( RadarSystemConfig.protcolType == ProtocolType.tcp) { sonarServer.activate(); }
		client1.activate();
		for( int i=1; i<=5; i++) {
			int v = client1.getDistance().getVal();
			System.out.println("execute getVal="+v);
			Utils.delay(100);
		}	
	}

	public void terminate() {
		System.out.println("SonarUsageMain | terminate" );
		if( RadarSystemConfig.protcolType == ProtocolType.tcp)  sonarServer.deactivate();
		else if( RadarSystemConfig.protcolType == ProtocolType.coap)   CoapApplServer.getServer().destroy();
	}
	
	public static void main( String[] args) throws Exception {
		SonarUsageMain  sys = new SonarUsageMain();	
		sys.configure();
		sys.execute();
		Utils.delay(500);
		sys.terminate();

	}
}

/*
 * 		RadarSystemConfig.simulation = true;
		RadarSystemConfig.sonarPort  = 8011;
		RadarSystemConfig.sonarDelay = 500;
		RadarSystemConfig.testing    = false;
		 
		String sonarPort    		 = "8011";
		ProtocolType protocol 		 = ProtocolType.tcp;
		String host     			 = "localhost" ;            
		
		ISonar sonar = SonarModel.create();			
		
		if( protocol == ProtocolType.tcp) {
			//int port = Integer.parseInt(sonarPort);
			EnablerAsServer sonarServer  = 
				new EnablerAsServer("sonarServer",RadarSystemConfig.sonarPort, 
						protocol, new SonarApplHandler("sonarH",sonar) );
			sonar.activate();
			sonarServer.activate()	;	
		}else if( protocol == ProtocolType.coap) {
			new SonarResourceCoap("sonar", sonar);
		}
		
		String nameUri = CoapApplServer.outputDeviceUri+"/sonar";
		String entry   = protocol==ProtocolType.coap ? nameUri : sonarPort;
		ISonar client1 = new SonarProxyAsClient("client1", host, entry, protocol );

		//sonarEnablerAsClient.activate();
		
		for( int i=1; i<=5; i++) {
			int v = client1.getDistance().getVal();
			System.out.println("Controller-simulated getVal="+v);
			Thread.sleep(100);
		}
		//sonarServer.deactivate();
 */


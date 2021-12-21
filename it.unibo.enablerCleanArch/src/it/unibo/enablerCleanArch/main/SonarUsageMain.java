package it.unibo.enablerCleanArch.main;

 
 
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.devices.EnablerSonarAsServer;
import it.unibo.enablerCleanArch.enablers.devices.SonarApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.SonarResourceCoap;
  

public class SonarUsageMain  {

private EnablerAsServer sonarServer;
private ISonar client1, client2;

	public void configure() {
		RadarSystemConfig.simulation  = true;
 		RadarSystemConfig.testing     = false;
 		RadarSystemConfig.sonarPort   = 8011;
		RadarSystemConfig.sonarDelay  = 500;
		RadarSystemConfig.protcolType = ProtocolType.coap;
 		RadarSystemConfig.pcHostAddr  = "localhost";
 		
 		configureTheSonarEnablerServer();
 		configureTheSonarProxyClients();
 		Colors.outappl("SonarUsageMain | configure done", Colors.ANSI_PURPLE  );
	}
	
	protected void configureTheSonarEnablerServer() {
		ISonar sonar = DeviceFactory.createSonar();
		if( RadarSystemConfig.protcolType == ProtocolType.tcp) {
			sonarServer  = new EnablerSonarAsServer("sonarServer",RadarSystemConfig.sonarPort, 
					RadarSystemConfig.protcolType, new SonarApplHandler("sonarH",sonar), sonar );		
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
		client2         = new SonarProxyAsClient("client2", host, entry, protocol );	
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
		Colors.outappl("SonarUsageMain | terminate", Colors.ANSI_PURPLE );
		if( RadarSystemConfig.protcolType == ProtocolType.tcp) {
			sonarServer.deactivate(); //stops also the sonar device
		}
		else if( RadarSystemConfig.protcolType == ProtocolType.coap) {
			client2.deactivate();
			CoapApplServer.getServer().destroy();
		}
	}
	
	public static void main( String[] args) throws Exception {
		SonarUsageMain  sys = new SonarUsageMain();	
		sys.configure();
		sys.execute();
		Utils.delay(500);
		sys.terminate();

	}
}


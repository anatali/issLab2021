package it.unibo.enablerCleanArch.enablers.devices;

 
 
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.CoapLedResource;
import it.unibo.enablerCleanArch.supports.coap.CoapSonarResource;
import it.unibo.enablerCleanArch.supports.coap.DeviceType;
  

public class SonarProxyAsClientMain  {
 
	public static void main( String[] args) throws Exception {
		RadarSystemConfig.simulation = true;
		RadarSystemConfig.sonarPort  = 8011;
		RadarSystemConfig.sonarDelay = 500;
		RadarSystemConfig.testing    = false;
		 
		String sonarPort    		 = "8011";
		ProtocolType protocol 		 = ProtocolType.coap;
		String host     			 = "localhost" ;            
		
		if( protocol == ProtocolType.tcp) {
			int port = Integer.parseInt(sonarPort);
			EnablerAsServer sonarServer  = 
				new EnablerAsServer("sonarServer",port, protocol, new SonarApplHandler("sonarH") );
			sonarServer.activate()	;	
		}else if( protocol == ProtocolType.coap) {
			new CoapSonarResource("sonar", DeviceType.output);
		}
		String nameUri = CoapApplServer.outputDeviceUri+"/sonar";
		String entry   = protocol==ProtocolType.coap ? nameUri : sonarPort;
		ISonar client1 = new SonarProxyAsClient("client1", host, entry, protocol );

		//sonarEnablerAsClient.activate();
		
		System.out.println("Controller-simulated doing getVal ..." );
		for( int i=1; i<=5; i++) {
			int v = client1.getDistance().getVal();
			System.out.println("Controller-simulated getVal="+v);
			Thread.sleep(100);
		}
		//sonarServer.deactivate();
		System.exit(0);
	}
}

 


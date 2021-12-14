package it.unibo.enablerCleanArch.enablers.devices;

 
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
  

public class SonarEnablerAsClientMain  {
 
	public static void main( String[] args) throws Exception {
		RadarSystemConfig.simulation = true;
		RadarSystemConfig.sonarPort  = 8011;
		RadarSystemConfig.sonarDelay = 500;
		RadarSystemConfig.testing    = false;
		 
		//ProtocolType protocol        = ProtocolType.tcp;
		ProtocolType protocol        = ProtocolType.coap;
		
		EnablerAsServer sonarServer  = 
				new EnablerAsServer("SonarAdapterEnablerAsServer",RadarSystemConfig.sonarPort, protocol, new SonarApplHandler("sonarH") );

		ISonar sonarEnablerAsClient = new SonarEnablerAsClient(
				"sonarEnablerAsClient", "localhost",RadarSystemConfig.sonarPort, protocol );

		sonarServer.activate();
		sonarEnablerAsClient.activate();
		
		System.out.println("Controller-simulated doing getVal ..." );
		for( int i=1; i<=5; i++) {
			int v = sonarEnablerAsClient.getDistance().getVal();
			System.out.println("Controller-simulated getVal="+v);
			Thread.sleep(100);
		}
		sonarServer.deactivate();
		System.exit(0);
	}
}

 


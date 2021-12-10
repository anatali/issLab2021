package it.unibo.enablerCleanArch.enablers;

import it.unibo.enablerCleanArch.adapters.SonarAdapterEnablerAsServer;
import it.unibo.enablerCleanArch.concur.NaiveApplHandler;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;
  

public class SonarEnablerAsClient extends EnablerAsClient{
	private ISonar sonar ;
	
	public SonarEnablerAsClient( String name, String host, int port, ProtocolType protocol, ISonar sonar ) {
		super( name,  host,  port, protocol );
		this.sonar = sonar;
	}
	
	public void handleMessagesFromServer( Interaction2021 conn ) throws Exception {
		while( true ) {
			//Colors.out(name + " | handleMessagesFromServer  wait for data ... "   );
			String cmd = conn.receiveMsg();
			//Colors.out( name + " | receives " + cmd );
			if( cmd.equals("activate")) {
				sonar.activate();
			}else if( cmd.equals("getVal")) {
				String data = ""+sonar.getVal();
				sendValueOnConnection(data);
			}
			else if( cmd.equals("deactivate")) {
				sonar.deactivate();
				break;
			}
		}
	}
	
	
	/*
	 * A rapid test ...
	 */
	public static void main( String[] args) throws Exception {
		RadarSystemConfig.simulation = true;
		RadarSystemConfig.sonarPort  = 8011;
		RadarSystemConfig.sonarDelay = 500;
		RadarSystemConfig.testing    = false;
		
		ISonar sonar = DeviceFactory.createSonar();
		
 		SonarAdapterEnablerAsServer sonarAdapter  = 
				new SonarAdapterEnablerAsServer("sonarAdapter",RadarSystemConfig.sonarPort, ProtocolType.tcp, new NaiveApplHandler("nah") );

 		new SonarEnablerAsClient(
				"SonarEnablerAsClient", "localhost",RadarSystemConfig.sonarPort, ProtocolType.tcp, sonar);

		sonarAdapter.activate();
		
		for( int i=1; i<=5; i++) {
			int v = sonarAdapter.getVal();
			Thread.sleep(500);
			System.out.println("Controller-simulated getVal="+v);
		}
		sonarAdapter.deactivate();
		System.exit(0);
	}
}

 


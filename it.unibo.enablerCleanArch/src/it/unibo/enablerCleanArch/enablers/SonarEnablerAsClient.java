package it.unibo.enablerCleanArch.enablers;

import it.unibo.enablerCleanArch.adapters.SonarAdapterEnablerAsServer;
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
		Colors.out(name + " | created "   );
	}
	
	public void handleMessagesFromServer( Interaction2021 conn ) throws Exception {
		while( true ) {
			//Colors.out(name + " | handleMessagesFromServer  wait for data ... "   );
			String cmd = conn.receiveMsg();
			//Colors.out( name + " | receives " + cmd );
			if( cmd.equals("activate")) {
				sonar.activate();
				//doClientOutWork();
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
	
	public static void main( String[] args) throws Exception {
		RadarSystemConfig.simulation = true;
		RadarSystemConfig.sonarPort  = 8011;
		RadarSystemConfig.sonarDelay = 500;
		RadarSystemConfig.testing    = false;
		
		ISonar sonar = DeviceFactory.createSonar();
		
		//SonarAdapterTcp sonarAdapter  = new SonarAdapterTcp("sonarAdapter","localhost",RadarSystemConfig.sonarPort );
		SonarAdapterEnablerAsServer sonarAdapter  = 
				new SonarAdapterEnablerAsServer("sonarAdapter",RadarSystemConfig.sonarPort, ProtocolType.tcp );

		//SonarClient sonarClient = 
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


/*
 * 	@Override
	protected Interaction2021 setConnection( String host, int port  ) throws Exception {
		Interaction2021 conn = TcpClient.connect(host,  port, 10);
		handleMessagesFromServer( conn );
		//Colors.out(name + " |  setConnection "  + conn );
		return conn;
		//Coap: attivo un SonarObserver che implementa getVal
	}

//Thread che attende messaggi di comando dal server
protected void handleMessagesFromServer( Interaction2021 conn) {
	new Thread() {
		public void run() {
			try {
				while( true ) {
					//Colors.out(name + " | handleMessagesFromServer  wait for data ... "   );
					String cmd = conn.receiveMsg();
					//Colors.out( name + " | receives " + cmd );
					if( cmd.equals("activate")) {
						sonar.activate();
						//doClientOutWork();
					}else if( cmd.equals("getVal")) {
						String data = ""+sonar.getVal();
						sendValueOnConnection(data);
					}
					else if( cmd.equals("deactivate")) {
						sonar.deactivate();
						break;
					}
				}
				Colors.out(name + " | handleMessagesFromServer  BYE "   );
			} catch (Exception e) {
				Colors.outerr( "SonarClient | handleMessagesFromServer  ERROR " + e.getMessage());
			}				
		}
	}.start();
}
*/


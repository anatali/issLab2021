package it.unibo.enablerCleanArch.enablers;

import it.unibo.enablerCleanArch.adapters.SonarAdapterTcp;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClient;
 

public class SonarEnablerAsClient extends EnablerAsClient{
	private ISonar sonar ;
	
	public SonarEnablerAsClient( String name, String host, int port, ISonar sonar ) {
		super( name,  host,  port );
		this.sonar = sonar;
		Colors.out(name + " | created "   );
	}
	 
	@Override
	protected Interaction2021 setConnection( String host, int port  ) throws Exception {
		Interaction2021 conn = TcpClient.connect(host,  port, 10);
		handleMessagesFromServer( conn );
		Colors.out(name + " |  setConnection "  + conn );
		//conn.forward("50");
		return conn;
		//Coap: attivo un SonarObserver che implementa getVal
	}

	
	protected void doClientOutWork() {
		
		new Thread() {
			public void run() {
				while( sonar.isActive() ){
				   try {
					   String data = ""+sonar.getVal();
					   //Colors.out( "SonarClient | doClientOutWork data= " + data );
					   sendValueOnConnection(data);
				   } catch (Exception e) {
						Colors.outerr( "SonarClient | doWork  ERROR " + e.getMessage());
					}
				}//while  								
			}
		}.start();
 		
	}
 
	//Thread che attende messaggi dal server
	protected void handleMessagesFromServer( Interaction2021 conn) {
		new Thread() {
			public void run() {
				try {
					while( true ) {
						Colors.out(name + " | handleMessagesFromServer  wait for data ... "   );
						String cmd = conn.receiveMsg();
						Colors.out( "SonarClient | receives " + cmd );
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
	
	
	public static void main( String[] args) throws Exception {
		RadarSystemConfig.simulation = true;
		RadarSystemConfig.sonarPort  = 8011;
		RadarSystemConfig.sonarDelay = 500;
		RadarSystemConfig.testing    = false;
		
		ISonar sonar = DeviceFactory.createSonar();
		
		SonarAdapterTcp sonarProxy  = new SonarAdapterTcp("sonarAdapter","localhost",RadarSystemConfig.sonarPort );

		//SonarClient sonarClient = 
				new SonarEnablerAsClient("sonarClient", "localhost",RadarSystemConfig.sonarPort, sonar);

		sonarProxy.activate();
		
		for( int i=1; i<=5; i++) {
			int v = sonarProxy.getVal();
			Thread.sleep(500);
			System.out.println("sonarValueFromAdapter="+v);
		}
		sonarProxy.deactivate();
	}
}



package it.unibo.enablerCleanArch.enablers;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.SonarConcrete;
import it.unibo.enablerCleanArch.domain.SonarMock;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClient;
 

public class SonarClient extends EnablerAsClient{
    private int numData           = 1;   //valore basso perchè bastano i ritardi di rete
    private int dataCounter       = 1;
	private boolean goon          = true;
	
	public SonarClient( String name, String host, int port ) {
		super( name,  host,  port );
		doWork();
	}
	 
	@Override
	protected Interaction2021 setProtocolClient( String host, int port  ) throws Exception {
		return TcpClient.connect(host,  port, 10);
		//Coap: attivo un SonarObserver che implementa getVal
	}

	protected void doWork() {
		try {
			if( ! RadarSystemConfig.simulation) {
				/*
				Process p             = Runtime.getRuntime().exec("sudo ./SonarAlone");
		        BufferedReader reader = new BufferedReader( new InputStreamReader(p.getInputStream()));	
			new Thread() {
				public void run() {
					try {
						generateAndSendValues( reader );
					}catch ( Exception e) {
						System.out.println( "SonarClient |  GEN ERROR " + e.getMessage());
						goon = false;
					}
				}
			}.start();*/
				ISonar sonar = new SonarConcrete();
				sonar.activate();
			}else {//devices simulated
				ISonar sonar = new SonarMock();
				sonar.activate();
	 	        while( sonar.isActive() ){
			        String data = ""+sonar.getVal();
			        sendValueOnConnection(data);
 			   }//while  		
			}

		} catch (Exception e) {
			System.out.println( "SonarClient |  SYSTEM ERROR " + e.getMessage());			 
		}	  		
	}
 	
	protected void generateAndSendValues( BufferedReader reader ) throws Exception{
  	        while( goon ){
		        String data = reader.readLine();
		        dataCounter++;
		        if( dataCounter % numData == 0 ) { //every numData ...
			        System.out.println("SonarClient | data=" + data );
 			        sendValueOnConnection(data);
		        }
		   }//while  		
	}


	
}

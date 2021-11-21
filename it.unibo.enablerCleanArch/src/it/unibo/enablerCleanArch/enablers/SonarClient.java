package it.unibo.enablerCleanArch.enablers;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClient;
import it.unibo.enablerCleanArch.useCases.LedAlarmUsecase;

public class SonarClient {
	private Interaction2021 conn;
	
	public SonarClient( String host, int port ) {
		try {
			conn = TcpClient.connect(host,  port);
			new Thread() {
				public void run() {
					sendRealValue();
				}
			}.start();
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}
	
	protected void sendRealValue() {
		try {
			Process p             = Runtime.getRuntime().exec("sudo ./SonarAlone");
	        BufferedReader reader = new BufferedReader( new InputStreamReader(p.getInputStream()));	
	        int numData           = 1;   //valore basso perchè bastano i ritardi di rete
	        int dataCounter       = 1;
	        
	        while( true ){
		        String data = reader.readLine();
		        dataCounter++;
		        if( dataCounter % numData == 0 ) { //every numData ...
			        //System.out.println("SonarClient | data=" + data );
		        	int v = Integer.parseInt(data);
		        	if( v < LedAlarmUsecase.DLIMIT ) System.out.println("SonarClient | ALARM !!!! " + data );
			        conn.forward( data );
		        }
		     }//while
		} catch (Exception e) {
			System.out.println( "SonarClient |  ERROR " + e.getMessage());
		}	  		
	}	
	
}

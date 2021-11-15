package it.unibo.enabler;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONObject;

import it.unibo.bls.devices.LedConcrete;
import it.unibo.bls.devices.LedMock;
import it.unibo.bls.devices.SonarConcrete;
import it.unibo.bls.interfaces.ILed;

/*
 *  
 * Un Thread che emette dati verso il Controller (dispatch)
 * Un Thread che pubblica dati (event)
 * Un ?? che fornisce dati a richiesta a un Controller remoto (request-response)
 */

public class SonarEnabler extends TcpEnabler{

private EnablerClient c_caller;

	public SonarEnabler(int port) throws Exception {
		super("sonarEnabler",port);
	}


	@Override
	protected void elaborate(String message) {
		System.out.println("sonarEnabler | elaborate " + message);
		try {
			JSONObject jsonObj = new JSONObject( message );
			//{"host":"localhost", "port":"8016"}
			String host = jsonObj.getString("host");
			String port = jsonObj.getString("port");
			c_caller    = new EnablerClient( host, Integer.getInteger(port).intValue() );
			new Thread() {
				public void run() {
					getValueAndSend();
				}
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}		 
	}
	
	
	protected void getValueAndSend() {
		/*
		try {
			Process p             = Runtime.getRuntime().exec("sudo ./SonarAlone");
	        BufferedReader reader = new BufferedReader( new InputStreamReader(p.getInputStream()));	
	        int numData           = 5;
	        int dataCounter       = 1;
	        
	        while( true ){
		        String data = reader.readLine();
		        dataCounter++;
		        if( dataCounter % numData == 0 ) { //every numData ...
			        System.out.println("sonarEnabler | data=" + data );
			        c_caller.forward( data );
		        }
		     }//while
		} catch (Exception e) {
			e.printStackTrace();
		}	  
		*/      
		 
		for( int i = 1; i<=8; i++) {
			int v = 10*i;
			try {
				c_caller.forward(""+v);
			} catch (Exception e) {
 				e.printStackTrace();
			}			
		} 
		
	}

	
}

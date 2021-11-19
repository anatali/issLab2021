package it.unibo.radarWithEnabler;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;
import it.unibo.enabler.ApplMessageHandler;
import it.unibo.enabler.TcpClient;
import it.unibo.enabler.TcpEnabler;

/*
 * Gestisce i messaggi ricevuti dall'enabler
 * Il primo messaggio include host e port su cui inviare i dati del sonar
 * realizzando una sorta di request-response
 */
class SonarMsgHandler extends ApplMessageHandler{
	private TcpClient c_caller; 	//
	private boolean simulated = true;
	
	public SonarMsgHandler(boolean simulated) {
		this.simulated = simulated;
	}
	@Override
	protected void elaborate(String message) {
		System.out.println("sonarEnabler | elaborate " + message);
		try {
			JSONObject jsonObj = new JSONObject( message );
			//{"host":"localhost", "port":"8016"}
			String host = jsonObj.getString("host");
			String port = jsonObj.getString("port");
			System.out.println("sonarEnabler | elaborate host="+host+ " port=" + port);
			int p       = Integer.parseInt(port);
			c_caller    = new TcpClient( host, p, null );
			System.out.println("sonarEnabler | elaborate c_caller to port:" + p);
			new Thread() {
				public void run() {
					if( simulated ) sendSimulatedValue();
					else sendRealValue();
				}
			}.start();
		} catch (Exception e) {
 			System.out.println("sonarEnabler | ERROR " + e.getMessage() );
		}		 
	}
	
	protected void sendSimulatedValue() {
		for( int i = 1; i<=8; i++) {
			int v = 10*i;
			try {
				if( c_caller != null ) c_caller.forward(""+v);
				Thread.sleep(1000);
			} catch (Exception e) {
 				System.out.println("sonarEnabler | getValueAndSend ERROR " + e.getMessage() );
			}			
		} 		
	}
	
	protected void sendRealValue() {
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
	}	
}
public class SonarEnabler extends TcpEnabler{
private static final boolean simulated = true;

	public SonarEnabler(int port, boolean simulated) throws Exception {
		super("sonarEnabler",port, new SonarMsgHandler(SonarEnabler.simulated) );
	}
	
}

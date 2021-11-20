package it.unibo.enablerCleanArch.adapters;

import org.json.JSONObject;

 
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClient;
import it.unibo.enablerCleanArch.supports.TcpServer;
 

/*
 * Adapter (working as a server) for an input device 
 */

class SonarMsgHandler extends ApplMessageHandler{
	private SonarAdapter sa; 	 
	
 
	public SonarMsgHandler( SonarAdapter sa) {
		super("sonarmh");
		this.sa = sa;
 	}

 	@Override
	protected void elaborate(String message) {
		System.out.println("sonarEnabler | elaborate " + message);
		try {
			int p  = Integer.parseInt(message);
			sa.setVal(p);			 
		} catch (Exception e) {
 			System.out.println("sonarEnabler | ERROR " + e.getMessage() );
		}		 
	}
}
	
public class SonarAdapter implements ISonar{
private int curVal       = -1;

	public SonarAdapter( ) {
		try {
 			new TcpServer( "saserver", RadarSystemConfig.sonarPort, new SonarMsgHandler( this) );
		} catch (Exception e) {
 			e.printStackTrace();
		}
 	}
 
	synchronized void setVal(int d){
		curVal = d;
		this.notify();
	}
	
	public synchronized void waitForUpdatedVal() {
		try {
			while( curVal < 0 ) wait();
 		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	
	@Override
	public int getVal() {  
		waitForUpdatedVal();
 		int v  = curVal;
 		curVal = -1;
		return v;
	}

}

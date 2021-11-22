package it.unibo.enablerCleanArch.adapters;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.TcpServer;
 

/*
 * Adapter (working as a server) for an input device 
 */
 

public class SonarAdapterServer extends ApplMessageHandler implements ISonar{
private int curVal       = -1;

	public SonarAdapterServer( String name ) {
		super(name);
		try {
 			new TcpServer( name+"Server", RadarSystemConfig.sonarPort,  this );
		} catch (Exception e) {
			System.out.println( name + " | ERROR " + e.getMessage() );
		}
 	}

	public void deactivate() {}	 //from ISonar
	public  void activate() {}   //from ISonar

	@Override  //from ISonar - called by the Controller
	public int getVal() {  
		waitForUpdatedVal();
 		int v  = curVal;
 		curVal = -1;
		return v;
	}
	
 	@Override  //from RequestHandler - called by the TcpApplMessageHandler created by the TcpServer
	public void elaborate(String message) {
		try {
			System.out.println( name + " | elaborate " + message);
			int p  = Integer.parseInt(message);
			setVal(p);			 
		} catch (Exception e) {
 			System.out.println( name + " | ERROR " + e.getMessage() );
		}		 
	}

	synchronized void setVal(int d){
		curVal = d;
		this.notify();
	}
	
	private synchronized void waitForUpdatedVal() {
		try {
			while( curVal < 0 ) wait();
 		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	


}

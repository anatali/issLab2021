package it.unibo.enablerCleanArch.adapters;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.TcpServer;
 

/*
 * Adapter (working as a server) for an input device 
 */
 

public class SonarAdapterServer extends EnablerAsServer implements ISonar{
private int curVal       = -1;

	public SonarAdapterServer( String name, int port ) {
		super(name, port);
 	}

	@Override	//from EnablerAsServer
 	public void setProtocolServer(int port, ApplMessageHandler enabler) throws Exception{
  		new TcpServer( name+"Server", port,  this );
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
	
 	@Override  //from ApplMessageHandler
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

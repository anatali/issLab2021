package it.unibo.enablerCleanArch.adapters;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.TcpServer;
 
 

/*
 * Adapter (working as a server) for an input device 
 */
 

public class SonarAdapterServer extends EnablerAsServer implements ISonar{
private int curVal       = -1;

	public SonarAdapterServer( String name, int port ) {
		super(name, port);
		Colors.out(name+" |  STARTS on" + port);
 	}

	@Override	//from EnablerAsServer
 	public void setProtocolServer( int port ) throws Exception{
  		new TcpServer( name+"Server", port,  this );
		//Coap: attivo un SonarObserver che implementa getVal (NO: lo deve fare il Controller!)
  		//che riceve this (un ApplMessageHandler)  di cui chiama  elaborate( msg )

		//new CoapInputObserver( name+"Server", port,  this );
 	}	
	@Override  //from ISonar
	public void deactivate() {}	
	@Override  //from ISonar
	public  void activate() {}    

	@Override //from ISonar
	public boolean isActive() {
 		return true;
	}
	
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
		this.notify();	//activates callers of  waitForUpdatedVal
	}
	
	private synchronized void waitForUpdatedVal() {
		try {
			while( curVal < 0 ) wait();
 		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}


 
}

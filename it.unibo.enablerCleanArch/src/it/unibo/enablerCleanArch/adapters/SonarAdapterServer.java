package it.unibo.enablerCleanArch.adapters;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.TcpServer;
 

/*
 * Adapter (working as a server) for an input device 
 */
 

public class SonarAdapterServer extends EnablerAsServer  { //extends ApplMessageHandler che ha conn
private SonarProxy sonarProxy;
 
	public SonarAdapterServer( String name,  int port, SonarProxy sonarProxy) throws Exception {
		super(name, port);
		Colors.out(name+" |  STARTS on " + port);
		this.sonarProxy = sonarProxy;
 	}

	@Override	//from EnablerAsServer FORSE MEGLIO METTERLO in EnablerAsServer(TCP)
 	public void setServerSupport( int port ) throws Exception{
		TcpServer server = new TcpServer( name+"Server", port,  this );
		server.activate();
		//Coap: attivo un SonarObserver che implementa getVal (NO: lo deve fare il Controller!)
  		//che riceve this (un ApplMessageHandler)  di cui chiama  elaborate( msg )

		//new CoapInputObserver( name+"Server", port,  this );
 	}	
	
 	@Override  //from ApplMessageHandler
	public void elaborate(String message) {
		try {
			//System.out.println( name + " | elaborate " + message);
			int p  = Integer.parseInt(message);
			sonarProxy.update( p );
		} catch (Exception e) {
			Colors.outerr( name + " | ERROR " + e.getMessage() );
		}		 
	}	
 	
 	
 	public void sendCommandToClient( String msg ) {
 		try {
 			Colors.out(name+" |  sendCommandToClient   " + msg + " conn=" + conn);
			if( conn != null ) conn.forward(msg);
		} catch (Exception e) {
			Colors.outerr( name + " | ERROR " + e.getMessage() );
		}
 	}
 	
 	
	/*
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
	*/

/*
	synchronized void setVal(int d){
		curVal = d;
		this.notify();	//activates callers of  waitForUpdatedVal
	}
	
	private synchronized void waitForUpdatedVal() {
		try {
			while( curVal < 0 ) wait();
 		} catch (InterruptedException e) {
 			Colors.outerr(name + " | ERROR " + e.getMessage());
		}		
	}
*/

 
}

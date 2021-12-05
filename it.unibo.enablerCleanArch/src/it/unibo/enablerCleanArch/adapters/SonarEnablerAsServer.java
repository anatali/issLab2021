package it.unibo.enablerCleanArch.adapters;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.TcpServer;
 

/*
 * Adapter (working as a server) for an input device 
 */
 

public class SonarEnablerAsServer extends EnablerAsServer  { //EnablerAsServer extends ApplMessageHandler che ha conn
private ApplMessageHandler handler;
 
	public SonarEnablerAsServer( String name,  int port, ApplMessageHandler handler) throws Exception {
		super(name, port, handler);
		this.handler = handler;
		Colors.out(name+" |  STARTS on " + port);
	}

	@Override	//from EnablerAsServer FORSE MEGLIO METTERLO in EnablerAsServer(TCP)
 	public void setServerSupport( int port, ApplMessageHandler handler ) throws Exception{
		TcpServer server = new TcpServer( "ServerTcp", port,  handler );
		server.activate();
		//Coap: attivo un SonarObserver che implementa getVal (NO: lo deve fare il Controller!)
  		//che riceve this (un ApplMessageHandler)  di cui chiama  elaborate( msg )

		//new CoapInputObserver( name+"Server", port,  this );
 	}	
	/*
 	@Override  //from ApplMessageHandler
	public void elaborate(String message) {
		try {
			//System.out.println( name + " | elaborate " + message);
			int p  = Integer.parseInt(message);
			//sonarProxy.update( p );
 
		} catch (Exception e) {
			Colors.outerr( name + " | ERROR " + e.getMessage() );
		}		 
	}	
 	*/
 	
 	public void sendCommandToClient( String msg ) {
 		try {
 			//Colors.out(name+" |  sendCommandToClient   " + msg + " conn=" + conn);
			if( handler.getConn()  != null ) handler.getConn().forward(msg);
		} catch (Exception e) {
			Colors.outerr( name + " | ERROR " + e.getMessage() );
		}
 	}
 	
 	 
}

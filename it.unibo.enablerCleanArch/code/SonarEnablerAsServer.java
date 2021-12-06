package it.unibo.enablerCleanArch.adapters;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.main.ProtocolType;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.TcpServer;
 

/*
 * Adapter (working as a server) for an input device 
 */
 

public class SonarEnablerAsServer  implements EnablerAsServer{ //EnablerAsServer extends ApplMessageHandler che ha conn
private ApplMessageHandler handler;
private String name;
//private int port;
	public SonarEnablerAsServer( String name ) throws Exception {
		this.name    = name;
		//this.port    = port;
		//this.handler = handler;
		//setServerSupport( port, handler, protocol);
		Colors.out(name+" |  CREATED   "  );
	}

	@Override	//from EnablerAsServer FORSE MEGLIO METTERLO in EnablerAsServer(TCP)
 	public void setServerSupport( int port, ApplMessageHandler handler, ProtocolType protocol ) throws Exception{
		this.handler = handler;
		if( protocol == ProtocolType.tcp ) {
			TcpServer server = new TcpServer( "ServerTcp", port,  handler );
			server.activate();
		}else if( protocol == ProtocolType.coap ) {
			//Coap: attivo un SonarObserver che implementa getVal (NO: lo deve fare il Controller!)
	  		//che riceve this (un ApplMessageHandler)  di cui chiama  elaborate( msg )
			//new CoapInputObserver( name+"Server", port,  this );			 
		}
		
 	}	
 	
	@Override
 	public void sendCommandToClient( String msg ) {
 		try {
 			//Colors.out(name+" |  sendCommandToClient   " + msg + " conn=" + conn);
			if( handler.getConn()  != null ) handler.getConn().forward(msg);
		} catch (Exception e) {
			Colors.outerr( name + " | ERROR " + e.getMessage() );
		}
 	}
 	
 	 
}

package it.unibo.enablerCleanArch.enablers;

import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.TcpServer;
 
/*
 * Attiva un server relativo al protocollo specificato (se non null)
 * lasciando la gestione dei messaggi inviati dai client alle classi specializzate
 * che si possono avvalere del metodo sendCommandToClient 
 * per inviare comandi e/o risposte a un client
 */
 
public abstract class EnablerAsServer extends ApplMessageHandler   { 
protected ApplMessageHandler handler;
protected ProtocolType protocol;
protected TcpServer serverTcp;

	public EnablerAsServer( String name, int port, ProtocolType protocol )   {
		super( name );
		try {
			this.protocol = protocol;
			handler       = this;
			if( protocol != null ) {
				setServerSupport( port, protocol  );
				Colors.out(name+" |  STARTED  on port=" + port + " protocol=" + protocol);
			}else Colors.out(name+" |  CREATED as ApplMessageHandler"  );
		} catch (Exception e) {
			Colors.outerr(name+" |  CREATE Error: " + e.getMessage()  );
		}
	}

 	protected void setServerSupport( int port, ProtocolType protocol  ) throws Exception{
		if( protocol == ProtocolType.tcp ) {
			serverTcp = new TcpServer( "ServerTcp_"+name, port,  handler );
			serverTcp.activate();
		}else if( protocol == ProtocolType.coap ) {
			//Coap: attivo un SonarObserver che implementa getVal (NO: lo deve fare il Controller!)
	  		//che riceve this (un ApplMessageHandler)  di cui chiama  elaborate( msg )
			//new CoapInputObserver( name+"Server", port,  this );			 
		}
	}	
 		 
 	public void sendCommandToClient( String msg ) {
 		try {
 			//Colors.out(name+" |  sendCommandToClient   " + msg + " conn=" + conn);
			if( handler.getConn()  != null ) handler.getConn().forward(msg);
		} catch (Exception e) {
			Colors.outerr( name + " | ERROR " + e.getMessage() );
		}
 	}
 	
 	public void deactivate() {
 		//Colors.out(name+" |  DEACTIVATE  "  );
		if( protocol == ProtocolType.tcp ) {
			serverTcp.deactivate();
		}else if( protocol == ProtocolType.coap ) {
		}		
 	}
  	 
}

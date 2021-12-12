package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.TcpServer;
 
/*
 * Attiva un server relativo al protocollo specificato (se non null)
 * lasciando la gestione dei messaggi inviati dai client alle classi specializzate
 * che si possono avvalere del metodo sendCommandToClient 
 * per inviare comandi e/o risposte a un client
 */
 
public class EnablerAsServer   {  //implements IApplMessageHandler 
private static int count=1;
protected String name;
protected ProtocolType protocol;
protected TcpServer serverTcp;
 
	public EnablerAsServer( String name, int port, ProtocolType protocol, IApplMsgHandler handler )   { //, String handlerClassName
 		try {
			this.name     			= name;
			this.protocol 			= protocol;
 			if( protocol != null ) {
				setServerSupport( port, protocol, handler  );
				Colors.out(name+" |  STARTED  on port=" + port + " protocol=" + protocol);
			}else Colors.out(name+" |  CREATED no protocol"  );
			//created = true;
		} catch (Exception e) {
			Colors.outerr(name+" |  CREATE Error: " + e.getMessage()  );
		}
	}

 	protected void setServerSupport( int port, ProtocolType protocol,IApplMsgHandler handler   ) throws Exception{
		if( protocol == ProtocolType.tcp ) {
			//serverTcp = new TcpServer( "EnabSrvTcp_"+count++, port,  this.getClass().getName(), this );
			serverTcp = new TcpServer( "EnabSrvTcp_"+count++, port,  handler );
			serverTcp.activate();
		}else if( protocol == ProtocolType.coap ) {
			//handler è una risorsa coap che aggiungo a CoapApplServer
			//Coap: attivo un SonarObserver che implementa getVal (NO: lo deve fare il Controller!)
	  		//che riceve this (un ApplMessageHandler)  di cui chiama  elaborate( msg )
			//new CoapInputObserver( name+"Server", port,  this );			 
		}
	}	
 	
  		 
	 
	public String getName() {
 		return name;
	}
 	public void  activate() {
 		
 	}
 	public boolean isActive() {
 		return true;
 	}
 
 	public void deactivate() {
 		//Colors.out(name+" |  DEACTIVATE  "  );
		if( protocol == ProtocolType.tcp ) {
			serverTcp.deactivate();
		}else if( protocol == ProtocolType.coap ) {
		}		
 	}
  	 
}

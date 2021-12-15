package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.TcpServer;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
 
/*
 * Attiva un server relativo al protocollo specificato (se non null)
 * lasciando la gestione dei messaggi inviati dai client alle classi specializzate
 * che si possono avvalere del metodo sendCommandToClient 
 * per inviare comandi e/o risposte a un client
 */
 
public class EnablerAsServer   {  
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
		} catch (Exception e) {
			Colors.outerr(name+" |  CREATE Error: " + e.getMessage()  );
		}
	}

 	protected void setServerSupport( int port, ProtocolType protocol,IApplMsgHandler handler   ) throws Exception{
		if( protocol == ProtocolType.tcp ) {
			//serverTcp = new TcpServer( "EnabSrvTcp_"+count++, port,  this.getClass().getName(), this );
			serverTcp = new TcpServer( "EnabSrvTcp_"+count++, port,  handler );
			//serverTcp.activate();
		}else if( protocol == ProtocolType.coap ) {
			CoapApplServer.getServer();	//Dove sono le risorse? handler (SonarApplHandler, ... )
		}
	}	
 	
 	public String getName() {
 		return name;
	}
 	public void  activate() {
		if( protocol == ProtocolType.tcp ) {
			serverTcp.activate();
		}else if( protocol == ProtocolType.coap ) {
		}		
 		
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

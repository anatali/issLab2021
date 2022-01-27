package it.unibo.enablerCleanArch.adapters;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.main.ProtocolType;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.TcpServer;
 

/*
 * Adapter (working as a server) for an input device 
 * Come EnablerAsServer : permette all'enabler client del sonar remoto (ecsr) di connettersi
 * Come ISonar : fornisce tutti i metodi inviando comandi remoti al client ecsr
 * Come ApplMessageHandler: gestisce i dati inviati dal client ecsr come risposta a getVal.
 * 						    attivando chi fosse in attesa della risposta
 */
 
public class SonarAdapterEnablerAsServerOld  extends ApplMessageHandler implements ISonar { 
private ApplMessageHandler handler;
private String name;
private int lastSonarVal = 0;		 
private boolean stopped  = true;	//mirror value
private boolean produced = false;
 
	public SonarAdapterEnablerAsServerOld( String name, int port, ProtocolType protocol ) throws Exception {
		super( name );
		setServerSupport( port, this, protocol);
		Colors.out(name+" |  STARTED  "  );
	}


 	protected void setServerSupport( int port, ApplMessageHandler handler, ProtocolType protocol ) throws Exception{
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
 	
	 
 	public void sendCommandToClient( String msg ) {
 		try {
 			//Colors.out(name+" |  sendCommandToClient   " + msg + " conn=" + conn);
			if( handler.getConn()  != null ) handler.getConn().forward(msg);
		} catch (Exception e) {
			Colors.outerr( name + " | ERROR " + e.getMessage() );
		}
 	}

	//From ApplMessageHandler
	@Override
	public void elaborate(String message) {  //receive a distance value after the request in getVal
		Colors.out(name + " |  elaborate " + message );
		lastSonarVal = Integer.parseInt( message );
		valueUpdated( );
	}

	
	//From ISonar
	@Override
	public void deactivate() {
		Colors.out(name + " | deactivate");
		try {
			sendCommandToClient("deactivate");
		} catch (Exception e) {		 
			Colors.outerr(name + " |  ERROR " + e.getMessage() );
		}		 
	}

	@Override
	public void activate() {
		sendCommandToClient("activate");
		stopped = false;
	}

 

	@Override
	public  int getVal() {
		sendCommandToClient("getVal");
		waitForUpdatedVal();
		return lastSonarVal;
	}

	private synchronized void waitForUpdatedVal() {
		try {
 			while( ! produced ) wait();
 			produced = false;
 		} catch (InterruptedException e) {
 			System.out.println("Sonar | waitForUpdatedVal ERROR " + e.getMessage() );
		}		
	}	

	@Override
	public boolean isActive() { 
		return stopped;
	}

	protected synchronized void valueUpdated( ){
		produced = true;
		this.notify();
	}
 	
 	 
}

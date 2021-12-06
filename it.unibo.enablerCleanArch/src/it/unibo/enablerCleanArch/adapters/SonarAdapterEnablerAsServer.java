package it.unibo.enablerCleanArch.adapters;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.supports.Colors;

 

/*
 * Adapter (working as a server) for an input device 
 * Come EnablerAsServer : permette all'enabler client del sonar remoto (ecsr) di connettersi
 * Come ISonar : fornisce tutti i metodi inviando comandi remoti al client ecsr
 * Come ApplMessageHandler: gestisce i dati inviati dal client ecsr come risposta a getVal.
 * 						    attivando chi fosse in attesa della risposta
 */
 
public class SonarAdapterEnablerAsServer  extends EnablerAsServer implements ISonar { 
private int lastSonarVal = 0;		 
private boolean stopped  = true;	//mirror value
private boolean produced = false;
 
	public SonarAdapterEnablerAsServer( String name, int port, ProtocolType protocol ) throws Exception {
		super( name, port, protocol );
 		Colors.out(name+" |  STARTED  "  );
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
		sendCommandToClient("deactivate"); 
		stopped = true;
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
 			Colors.outerr(name + " | waitForUpdatedVal ERROR " + e.getMessage() );
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

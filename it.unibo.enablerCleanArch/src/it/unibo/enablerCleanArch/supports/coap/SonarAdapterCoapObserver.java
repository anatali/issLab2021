package it.unibo.enablerCleanArch.supports.coap;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.supports.Colors;
 

/*
 * Adapter Coap for the sonar
 */
public class SonarAdapterCoapObserver implements ISonar, CoapHandler{
private IDistance curVal = null;
private CoapSupport cps ;

	public SonarAdapterCoapObserver( String hostAddr, String resourceUri   ) {
		
 		System.out.println("SonarAdapterCoapObserver |  STARTS  " + resourceUri );
 		//Attivo un Observer che implementa ISonar (per il Controller)
 		cps = new CoapSupport(hostAddr, resourceUri );
		Colors.out("SonarAdapterCoapObserver |  cps  " + cps );
		cps.observeResource(this);		
 	}

	// From CoapHandler
	@Override
	public void onLoad(CoapResponse response) {
		System.out.println("SonarAdapterCoapObserver | response " +  response.getResponseText() );
		elaborate(response.getResponseText());
	}

	@Override
	public void onError() {
		System.out.println("SonarAdapterCoapObserver | onError ERROR " );	
	}
	
	//from ISonar
	
	public void deactivate() { 
		try {
			cps.updateResource( "deactivate" );
		} catch (Exception e) {
			Colors.outerr("SonarAdapterCoapObserver | deactivate ERROR " + e.getMessage());
		}
	}	 
	public  void activate() {
		try {
			cps.updateResource( "activate" );
		} catch (Exception e) {
			Colors.outerr("SonarAdapterCoapObserver | activate ERROR " + e.getMessage());
		}		
		
	}    

	@Override //from ISonar
	public boolean isActive() {
 		return true;
	}
	
	@Override   //called by the Controller
	public IDistance getDistance() {  
		waitForUpdatedVal();
 		IDistance v  = curVal;
 		curVal = null;
		return v;
	}
	
 	protected void elaborate(String message) {
		try {
			System.out.println("SonarAdapterCoapObserver | elaborate " + message);
			if( message.length() == 0 ) return;
			int p  = Integer.parseInt(message);
			if( p >= 0 ) setVal(p);			 
		} catch (Exception e) {
 			System.out.println( "SonarAdapterCoapObserver | ERROR " + e.getMessage() );
		}		 
	}

	synchronized void setVal(int d){
		curVal = new Distance( d );
		this.notify();	//activates callers of  waitForUpdatedVal
	}
	
	private synchronized void waitForUpdatedVal() {
		try {
			while( curVal == null ) wait();
 		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}



 
}

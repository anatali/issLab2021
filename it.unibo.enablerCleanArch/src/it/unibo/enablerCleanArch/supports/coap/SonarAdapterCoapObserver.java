package it.unibo.enablerCleanArch.supports.coap;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import it.unibo.enablerCleanArch.domain.*;
 

/*
 * Adapter Coap for the sonar
 */
public class SonarAdapterCoapObserver implements ISonar, CoapHandler{
private int curVal       = -1;

	public SonarAdapterCoapObserver( String hostAddr, String resourceUri   ) {
		
 		System.out.println("SonarAdapterCoapObserver |  STARTS  "  );
 		//Attivo un Observer che implementa ISonar (per il Controller)
 		System.out.println("SonarAdapterCoapObserver | resourceUri= " + resourceUri );
		CoapSupport cps = new CoapSupport(hostAddr, resourceUri );
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
		System.out.println("SonarAdapterCoapObserver | ERROR " );	
	}
	
	//from ISonar
	
	public void deactivate() {}	 
	public  void activate() {}    

	@Override //from ISonar
	public boolean isActive() {
 		return true;
	}
	
	@Override   //called by the Controller
	public int getVal() {  
		waitForUpdatedVal();
 		int v  = curVal;
 		curVal = -1;
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

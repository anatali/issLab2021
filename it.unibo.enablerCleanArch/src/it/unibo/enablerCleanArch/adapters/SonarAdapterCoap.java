package it.unibo.enablerCleanArch.adapters;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.coap.CoapApplObserver;
 
 

/*
 * Adapter Coap for the sonar
 */
 

public class SonarAdapterCoap extends ApplMessageHandler implements ISonar{
private int curVal       = -1;

	public SonarAdapterCoap( String hostAddr, String resourceName   ) {
		super("SonarAdapterCoap");
 		System.out.println( name + " |  STARTS  "  );
 		//Coap: attivo un SonarObserver che implementa getVal (NO: lo deve fare il Controller!)
  		//che riceve this (un ApplMessageHandler)  di cui chiama  elaborate( msg )
		
 		new CoapApplObserver( hostAddr, resourceName, this );
 	}
 	
	public void deactivate() {}	 //from ISonar
	public  void activate() {}   //from ISonar

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
	
 	@Override  //from ApplMessageHandler
	public void elaborate(String message) {
		try {
			System.out.println( name + " | elaborate " + message);
			if( message.length() == 0 ) return;
			int p  = Integer.parseInt(message);
			if( p >= 0 ) setVal(p);			 
		} catch (Exception e) {
 			System.out.println( name + " | ERROR " + e.getMessage() );
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

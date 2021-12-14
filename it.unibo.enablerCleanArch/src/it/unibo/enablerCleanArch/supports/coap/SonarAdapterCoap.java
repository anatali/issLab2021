package it.unibo.enablerCleanArch.supports.coap;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
 
 

/*
 * Adapter Coap for the sonar
 * PROLISSO: può fare tutto un observer
 */
 

public class SonarAdapterCoap extends ApplMsgHandler implements ISonar{
private IDistance curVal = null;

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
	public IDistance getDistance() {  
		waitForUpdatedVal();
 		IDistance v  = curVal ;
 		curVal = null;
		return v;
	}
	
 	@Override  //from ApplMessageHandler
	public void elaborate(String message, Interaction2021 conn) {
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
		curVal = new Distance(d);
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

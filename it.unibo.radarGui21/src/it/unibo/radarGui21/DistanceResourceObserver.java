package it.unibo.radarGui21;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.observe.ObserveRelation;

public class DistanceResourceObserver implements CoapHandler{
  	
	public DistanceResourceObserver(String address, String path) { //"coap://localhost:5683/" + path
		String url        = address + "/" + path;
		CoapClient client = new CoapClient( url );
		System.out.println("DistanceResourceObserver | STARTS url=" +  url ); //+ " client=" + client );
		client.setTimeout( 1000L );	
		client.observe( this  );
	}

	@Override
	public void onLoad(CoapResponse response) {
		 String msg    = response.getResponseText();
		 System.out.println("DistanceResourceObserver  |  observes  " + msg);
		  
	}

	@Override
	public void onError() {
		System.out.println("DistanceResourceObserver  |  FAILED  ");
	}
	
	
}

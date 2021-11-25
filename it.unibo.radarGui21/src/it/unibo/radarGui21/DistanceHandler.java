package it.unibo.radarGui21;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;

public class DistanceHandler implements CoapHandler {
 
	@Override public void onLoad(CoapResponse response) {
		 String msg   = response.getResponseText();
		 ApplMessage m = new ApplMessage( msg );
		 //System.out.println("DistanceHandler " + m.msgContent());	//sonar(d)
		 String distance = ((Struct) Term.createTerm(m.msgContent())).getArg(0).toString();
		 radarPojo.radarSupport.update(distance,"0");
  	}					
	@Override public void onError() {
		System.err.println("SonarDataHandler  |  FAILED (press enter to exit)");
	}
}
 

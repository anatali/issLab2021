package it.unibo.radarGui21;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;

public class DistanceHandler implements CoapHandler {
	private IRadarGui radar;

	public static void showDataOnGui(String msg, IRadarGui radar){
		try {  //Normally we handle structured message strings
			ApplMessage m   = new ApplMessage( msg );
			//System.out.println("DistanceHandler " + m.msgContent());	//sonar(d)
			String distance = ((Struct) Term.createTerm(m.msgContent())).getArg(0).toString();
			radar.update(distance, "0");
		}catch( Exception e ){ //Otherwise we handle simple integers
			radar.update(msg, "0");
		}

	}
	public DistanceHandler(IRadarGui radar){
		this.radar = radar;
	}
	@Override
	public void onLoad(CoapResponse response) {
		 String msg    = response.getResponseText();
		 showDataOnGui( msg, radar );
  	}
	@Override public void onError() {
		System.err.println("SonarDataHandler  |  FAILED (press enter to exit)");
	}
}
 

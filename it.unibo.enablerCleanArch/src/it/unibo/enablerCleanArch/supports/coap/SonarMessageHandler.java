package it.unibo.enablerCleanArch.supports.coap;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.IRadarGui;
import it.unibo.enablerCleanArch.domain.RadarGui;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
 
public class SonarMessageHandler extends ApplMessageHandler{
	private IRadarGui radar;

	public SonarMessageHandler(String name) {
		super(name);
		radar = new RadarGui();
 	}

	@Override
	public void elaborate(String message) {
		System.out.println(name + " | elaborate " + message );
		showDataOnGui( message );
	}
	
	
	public  void showDataOnGui( String msg ){
		try {  //Normally we handle structured message strings
			ApplMessage m   = new ApplMessage( msg );
			//System.out.println("DistanceHandler " + m.msgContent());	//sonar(d)
			String distance = ((Struct) Term.createTerm(m.msgContent())).getArg(0).toString();
			radar.update(distance, "0");
		}catch( Exception e ){ //Otherwise we handle simple integers
			if( msg.length() > 0 ) radar.update(msg, "0");
			else System.out.println("showDataOnGui ERRORRRRRRRRRR: empty String");
		}

	}


}

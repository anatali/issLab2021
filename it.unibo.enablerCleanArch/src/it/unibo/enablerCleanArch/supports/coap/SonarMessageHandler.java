package it.unibo.enablerCleanArch.supports.coap;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.IRadarDisplay;
import it.unibo.enablerCleanArch.domain.RadarDisplay;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
 
public class SonarMessageHandler extends ApplMessageHandler{
	private IRadarDisplay radar;
/*
	public SonarMessageHandler(String name) {
		super(name);
		radar = new RadarGui();
 	}

	public SonarMessageHandler(Interaction2021 conn) {
		super(conn);
	}
*/	
	@Override
	public void elaborate(String message) {
		System.out.println(name + " | elaborate " + message );
		showDataOnGui( message );
	}
	
	@Override
	public void elaborate(String message, Interaction2021 conn) {
		// TODO Auto-generated method stub
		
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

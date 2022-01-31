package it.unibo.enablerCleanArchapplHandlers;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.IRadarDisplay;
import it.unibo.enablerCleanArch.domain.RadarDisplay;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Interaction2021;
 
public class SonarDistanceHandler extends ApplMsgHandler{
	private IRadarDisplay radar;	
	public SonarDistanceHandler(String name) {
		super(name);
		radar = RadarDisplay.getRadarDisplay();
	}

	@Override
	public void elaborate( ApplMessage message, Interaction2021 conn ) {}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.out(name + " | elaborate " + message );
		showDataOnGui( message );
	}
	
	public  void showDataOnGui( String msg ){
		try {  //Normally we handle structured message strings
			ApplMessage m   = new ApplMessage( msg );
			//Colors.out("DistanceHandler " + m.msgContent());	//sonar(d)
			String distance = ((Struct) Term.createTerm(m.msgContent())).getArg(0).toString();
			radar.update(distance, "0");
		}catch( Exception e ){ //Otherwise we handle simple integers
			if( msg.length() > 0 ) radar.update(msg, "0");
			else ColorsOut.outerr("showDataOnGui ERROR: empty String");
		}
	}

}

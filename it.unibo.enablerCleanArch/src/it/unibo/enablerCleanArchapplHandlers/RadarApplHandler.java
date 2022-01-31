package it.unibo.enablerCleanArchapplHandlers;
 
import org.json.JSONObject;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.IRadarDisplay;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class RadarApplHandler extends ApplMsgHandler {
IRadarDisplay radar;

	public RadarApplHandler(String name, IRadarDisplay radar) {
		super(name);
		this.radar = radar; 
	}

	@Override
	public void elaborate( ApplMessage message, Interaction2021 conn ) {}
	
 	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.out(name + " | elaborate " + message + " conn=" + conn);
		//{ "distance" : 90 , "angle" : 90 }
		JSONObject jsonObj   = new JSONObject(message);	
		String distance = ""+jsonObj.getInt("distance");
		radar.update( distance, "90" );
	}

}

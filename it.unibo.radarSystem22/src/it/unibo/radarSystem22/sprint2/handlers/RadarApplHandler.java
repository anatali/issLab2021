package it.unibo.radarSystem22.sprint2.handlers;
 
import org.json.JSONObject;
import it.unibo.comm2022.ApplMsgHandler;
import it.unibo.comm2022.interfaces.IApplMessage;
import it.unibo.comm2022.interfaces.Interaction2021;
import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;
import it.unibo.radarSystem22.domain.utils.ColorsOut;


public class RadarApplHandler extends ApplMsgHandler {
IRadarDisplay radar;

	public RadarApplHandler(String name, IRadarDisplay radar) {
		super(name);
		this.radar = radar; 
	}

	@Override
	public void elaborate( IApplMessage message, Interaction2021 conn ) {}
	
 	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.out(name + " | elaborate " + message + " conn=" + conn);
		//{ "distance" : 90 , "angle" : 90 }
		JSONObject jsonObj   = new JSONObject(message);	
		String distance = ""+jsonObj.getInt("distance");
		radar.update( distance, "90" );
	}

}

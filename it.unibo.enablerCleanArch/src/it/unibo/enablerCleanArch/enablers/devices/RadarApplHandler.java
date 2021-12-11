package it.unibo.enablerCleanArch.enablers.devices;
 
import it.unibo.enablerCleanArch.domain.IRadarDisplay;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class RadarApplHandler extends ApplMsgHandler {
IRadarDisplay radar;

	public RadarApplHandler(String name) {
		super(name);
		radar = it.unibo.enablerCleanArch.domain.RadarDisplay.create();
	}
	
 	@Override
	public void elaborate(String message, Interaction2021 conn) {
		//System.out.println(name + " | elaborate " + message + " conn=" + conn);
		String distance = message;
		radar.update( distance, "90" );
	}

}

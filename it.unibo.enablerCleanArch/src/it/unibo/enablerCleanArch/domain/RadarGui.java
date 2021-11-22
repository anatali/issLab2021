package it.unibo.enablerCleanArch.domain;

import radarPojo.radarSupport;

public class RadarGui implements IRadarGui{
private String curDistance = "0";
private static boolean created = false; //singleton
	//Factory method
	public static IRadarGui create(){
		return new RadarGui();
	}
	
	public RadarGui() {
		if( ! created ) {
			radarSupport.setUpRadarGui();
			created = true;
		}
		 
	}
	@Override
	public void update(String distance, String angle) {	 
		curDistance =  distance;
		radarSupport.update(distance,angle);
	}
	
	//ADDED for testing
	//-------------------------------------------------
	public int getCurDistance() {
		return Integer.parseInt(curDistance);
	}

}

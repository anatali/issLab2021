package it.unibo.enablerCleanArch.domain;

import radarPojo.radarSupport;

public class RadarDisplay implements IRadarDisplay{
private String curDistance = "0";
private static boolean created = false; //singleton
	//Factory method
	public static IRadarDisplay create(){
		return new RadarDisplay();
	}
	
	public RadarDisplay() {
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

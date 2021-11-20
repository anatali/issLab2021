package it.unibo.enablerCleanArch.domain;

import radarPojo.radarSupport;

public class RadarGui implements IRadarGui{

	//Factory method
	public static IRadarGui create(){
		return new RadarGui();
	}
	
	public RadarGui() {
		radarSupport.setUpRadarGui();
	}
	@Override
	public void update(String distance, String angle) {	 
		radarSupport.update(distance,angle);
	}

}

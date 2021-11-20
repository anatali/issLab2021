package it.unibo.enablerCleanArch.adapters;

 
import it.unibo.enablerCleanArch.domain.IRadarGui;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class RadarGuiAdapter implements IRadarGui {
 

	public RadarGuiAdapter(  ) {
 			//radar =  object able to send messages 
 	}

	@Override
	public void update(String distance, String angle) {		 
		//radar.update(distance,angle); 		
	}
	
	
}

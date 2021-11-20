package it.unibo.enablerCleanArch.adapters;

 
import it.unibo.enablerCleanArch.domain.IRadar;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class RadarAdapter implements IRadar {

	public RadarAdapter( ) {
		if( RadarSystemConfig.RadarGuieRemote)  {
			//led = LedMock.create();
		}else {
			radarPojo.radarSupport.setUpRadarGui(); 
		}
	}

	@Override
	public void update(String distance, String angle) {
		if( RadarSystemConfig.RadarGuieRemote)  {
			//led = LedMock.create();
		}else {
			radarPojo.radarSupport.update(distance,angle); 
		}		
		
	}
	
	
}

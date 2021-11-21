package it.unibo.enablerCleanArch.useCases;
import it.unibo.enablerCleanArch.domain.*;

public class RadarGuiUsecase {
  
	public static void doUseCase( IRadarGui radar, int d ) {
		System.out.println("RadarGuiUsecase | sonar data=" + d);
		radar.update(""+d, "90");
  	}	
 
}

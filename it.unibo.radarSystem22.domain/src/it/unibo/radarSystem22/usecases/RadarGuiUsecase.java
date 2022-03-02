package it.unibo.radarSystem22.usecases;

import it.unibo.radarSystem22.interfaces.IDistance;
import it.unibo.radarSystem22.interfaces.IRadarDisplay;

public class RadarGuiUsecase {
 
	public static void doUseCase( IRadarDisplay radar, IDistance d ) {	    
		//ColorsOut.out("RadarGuiUsecase |  doUseCase  d=" + d.getVal(), Colors.ANSI_YELLOW);
		if( radar != null ) {
			int v = d.getVal() ;
			radar.update(""+v, "30");
		}
  	}	
}

package it.unibo.radarSystem22.actors.businessLogic;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.IRadarDisplay;

public class RadarGuiUsecase {
 
	public static void doUseCase( IRadarDisplay radar, IDistance d ) {	    
		//ColorsOut.out("RadarGuiUsecase |  doUseCase  d=" + d.getVal(), ColorsOut.BLUE);
		if( radar != null ) {
			int v = d.getVal() ;
			radar.update(""+v, "30");
		}
  	}	
}

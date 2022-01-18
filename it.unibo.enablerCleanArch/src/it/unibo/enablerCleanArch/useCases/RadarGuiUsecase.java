package it.unibo.enablerCleanArch.useCases;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.supports.Colors;

public class RadarGuiUsecase {
 
	public static void doUseCase( IRadarDisplay radar, IDistance d ) {
	    
		//Colors.out("RadarGuiUsecase |  doUseCase  d=" + d.getVal(), Colors.ANSI_YELLOW);
		if( radar != null ) {
			int v = d.getVal() ;
			radar.update(""+v, "30");
		}
  	}	
}

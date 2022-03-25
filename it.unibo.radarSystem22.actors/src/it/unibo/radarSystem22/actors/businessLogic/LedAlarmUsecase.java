package it.unibo.radarSystem22.actors.businessLogic;

import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
 

public class LedAlarmUsecase {
 	public static void doUseCase(ILed led, IDistance d) {
 		try {
 			ColorsOut.out("LedAlarmUsecase | sonar distance=" + d + " " + BusinessLogicConfig.DLIMIT);
			if( d.getVal() <  BusinessLogicConfig.DLIMIT ) led.turnOn(); else  led.turnOff();
 		} catch (Exception e) {
 			ColorsOut.outerr("LedAlarmUsecase | ERROR " + e.getMessage() );
		}					
 	}
 
}

package it.unibo.enablerCleanArch.useCases;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;

public class LedAlarmUsecase {
 	public static void doUseCase(ILed led, IDistance d) {
 		try {
 			//Colors.out("LedAlarmUsecase | sonar distance=" + d);
			if( d.getVal() <  RadarSystemConfig.DLIMIT ) led.turnOn(); else  led.turnOff();
 		} catch (Exception e) {
 			ColorsOut.outerr("LedAlarmUsecase | ERROR " + e.getMessage() );
		}					
 	}
 
}

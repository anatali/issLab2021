package it.unibo.enablerCleanArch.useCases;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class LedAlarmUsecase {
 
	
	public static void doUseCase(ILed led, int d) {
 		try {
 			System.out.println("LedAlarmUsecase | sonar distance=" + d);
			if( d <  RadarSystemConfig.DLIMIT ) led.turnOn(); else  led.turnOff();
 		} catch (Exception e) {
 			System.out.println("LedAlarmUsecase | ERROR " + e.getMessage() );
		}					
 	}
 
}

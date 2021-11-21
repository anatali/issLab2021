package it.unibo.enablerCleanArch.useCases;
import it.unibo.enablerCleanArch.domain.*;

public class LedAlarmUsecase {
	public static final int DLIMIT = 15;	
	
	public static void doUseCase(ILed led, int d) {
 		try {
 			System.out.println("LedAlarmUsecase | sonar distance=" + d);
			if( d <  DLIMIT ) led.turnOn(); else  led.turnOff();
 		} catch (Exception e) {
		    e.printStackTrace();
		}					
 	}
 
}

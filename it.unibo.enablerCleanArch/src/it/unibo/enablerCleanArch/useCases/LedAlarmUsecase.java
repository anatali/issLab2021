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
 
	/*
	public static void activate(ILed led, ISonar sonar) {
		new Thread() {
			public void run() {
				try {
					while( true ) {
						int d = sonar.getVal();	//ricevo dato dal sonar
						System.out.println("LedAlarm | sonar data=" + d);
						if( d <  DLIMIT ) led.turnOn(); 
						else  led.turnOff();
						Thread.sleep(1000);
					}
				} catch (Exception e) {
		 			e.printStackTrace();
				}					
			}
		}.start();
	}
 	*/
}

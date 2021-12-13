package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.useCases.LedAlarmUsecase;
import it.unibo.enablerCleanArch.useCases.RadarGuiUsecase;

/*
 * Il Controller riceve dati dal sonar e attiva gli use cases
 */
public class Controller {
	
	public static void activate( ILed led, ISonar sonar,IRadarDisplay radar) {
		System.out.println("Controller | activate"  );
		new Thread() {
			public void run() { 
				try {
					System.out.println("Controller | STARTS"  );
					while( sonar.isActive() ) {
						IDistance d = sonar.getDistance(); //bloccante
 						LedAlarmUsecase.doUseCase( led,  d  );  //Meglio inviare un msg su una coda
						RadarGuiUsecase.doUseCase( radar,d  );	 
						System.out.println("sonar data=" + d + " ledState=" + led.getState());
						//Thread.sleep(1000);   //Rimuovere se sonar.getVal è bloccante
					}
					System.out.println("Controller | BYE"  );
				} catch (Exception e) {
		 			e.printStackTrace();
				}					
			}
		}.start();
		
	}
 }

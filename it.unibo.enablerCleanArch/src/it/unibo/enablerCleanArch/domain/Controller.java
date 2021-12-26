package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.useCases.LedAlarmUsecase;
import it.unibo.enablerCleanArch.useCases.RadarGuiUsecase;

/*
 * Il Controller riceve dati dal sonar e attiva gli use cases
 */
public class Controller {
	
	public static void activate( ILed led, ISonar sonar,IRadarDisplay radar) {
 		new Thread() {
			public void run() { 
				try {
					System.out.println("Controller | STARTS "    );
					boolean a = sonar.isActive();
					System.out.println("Controller | STARTS " + a );
					while( sonar.isActive() ) {
						IDistance d = sonar.getDistance(); //bloccante
						System.out.println("Controller | d=" + d  );
						RadarGuiUsecase.doUseCase( radar,d  );	//
 						LedAlarmUsecase.doUseCase( led,  d  );  //Meglio inviare un msg su una coda
 					}
					System.out.println("Controller | BYE"  );
				} catch (Exception e) {
		 			Colors.outerr("ERROR"+e.getMessage());
				}					
			}
		}.start();
		
	}
 }

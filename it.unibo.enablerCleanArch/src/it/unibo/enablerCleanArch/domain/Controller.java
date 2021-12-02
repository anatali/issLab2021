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
						int d = sonar.getVal(); //bloccante
						System.out.println("Controller | sonar data=" + d);
						//if( d == -1 ) break;
						LedAlarmUsecase.doUseCase( led,  d  );  //Meglio inviare un msg su una coda
						RadarGuiUsecase.doUseCase( radar,d  );	//Richiede comunicazione via rete
						//Thread.sleep(1000);   //Rimuovere se sonar.getVal � bloccante
					}
					System.out.println("Controller | BYE"  );
				} catch (Exception e) {
		 			e.printStackTrace();
				}					
			}
		}.start();
		
	}
 }

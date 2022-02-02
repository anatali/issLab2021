package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.useCases.LedAlarmUsecase;
import it.unibo.enablerCleanArch.useCases.RadarGuiUsecase;

/*
 * Il Controller riceve dati dal sonar e attiva gli use cases
 */
public class Controller {
private boolean activateTheSonar = false;

	public Controller(boolean activateTheSonar ) {
		this.activateTheSonar  = activateTheSonar;
	}

	public void activate( ILed led, ISonar sonar,IRadarDisplay radar) {
 		new Thread() {
			public void run() { 
				try {
					System.out.println("Controller | STARTS sonar=" + sonar   );
					if( activateTheSonar ) sonar.activate();
					boolean sonarActive = sonar.isActive();
					ColorsOut.outappl("Controller | STARTS " + sonarActive , ColorsOut.BLUE);
					if( sonarActive ) {
						for( int i=1; i<=90; i++) {
						//while( sonar.isActive() ) {
							IDistance d = sonar.getDistance(); //potrebbe essere bloccante
							ColorsOut.outappl("Controller | d="+d +" i=" + i, ColorsOut.BLUE  );
							if( radar != null) RadarGuiUsecase.doUseCase( radar,d  );	//
	 						LedAlarmUsecase.doUseCase( led,  d  );  //Meglio inviare un msg su una coda
	 						Utils.delay(RadarSystemConfig.sonarDelay);  //Al rtimo della generazione ...
	 					}
					}
					sonar.deactivate();
					ColorsOut.outappl("Controller | BYE", ColorsOut.BLUE  );
					//System.exit(0);
				} catch (Exception e) {
		 			ColorsOut.outerr("ERROR"+e.getMessage());
				}					
			}
		}.start();
		
	}
 }

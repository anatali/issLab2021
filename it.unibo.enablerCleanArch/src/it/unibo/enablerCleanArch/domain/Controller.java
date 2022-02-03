package it.unibo.enablerCleanArch.domain;

import java.util.function.Consumer;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.useCases.LedAlarmUsecase;
import it.unibo.enablerCleanArch.useCases.RadarGuiUsecase;


public class Controller {
private ILed led;
private ISonar sonar;
private IRadarDisplay radar;
private ActionFunction endFun;

	public static Controller create(ILed led, ISonar sonar,IRadarDisplay radar ) {
		return new Controller( led,  sonar, radar  );
	}
	
	private Controller( ILed led, ISonar sonar,IRadarDisplay radar ) {
		this.led    = led;
		this.sonar  = sonar;
		this.radar  = radar;
		//this.endFun = endFun;
	}
	
	
	public void start( ActionFunction endFun, int n ) {
		this.endFun = endFun;
		ColorsOut.outappl("Controller | start with endFun=" + endFun , ColorsOut.BLUE);
		sonar.activate();
		activate( n );
	}
	

	/*
	 * Il Controller riceve dati dal sonar e attiva gli use cases
	 */
	protected void activate( int limit ) {
 		new Thread() {
			public void run() { 
				try {
  					boolean sonarActive = sonar.isActive();
					ColorsOut.outappl("Controller | STARTS " + sonarActive , ColorsOut.BLUE);
					if( sonarActive ) {
						for( int i=1; i<=limit; i++) {
						//while( sonar.isActive() ) {
							IDistance d = sonar.getDistance(); //potrebbe essere bloccante
							ColorsOut.outappl("Controller | d="+d +" i=" + i, ColorsOut.BLUE  );
							if( radar != null) RadarGuiUsecase.doUseCase( radar,d  );	//
	 						LedAlarmUsecase.doUseCase( led,  d  );  //Meglio inviare un msg su una coda
	 						Utils.delay(RadarSystemConfig.sonarDelay);  //Al ritmo della generazione ...
	 					}
					}				
					//ColorsOut.outappl("Controller | BYE", ColorsOut.BLUE  );
					endFun.run("Controller | BYE ");
					//System.exit(0);
				} catch (Exception e) {
		 			ColorsOut.outerr("ERROR"+e.getMessage());
				}					
			}
		}.start();
		
	}
 }

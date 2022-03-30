package it.unibo.radarSystem22.actors.businessLogic;

 
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.domain.interfaces.*;
 

public class ControllerOnPc {
private ILed led;
private ISonar sonar;
private IRadarDisplay radar;
private ActionFunction endFun;

/*
 * Controller che interagisce con dispositivi Proxy o actor embedded
 */
	public static ControllerOnPc create(ILed led, ISonar sonar,IRadarDisplay radar ) {
		return new ControllerOnPc( led,  sonar, radar  );
	}
	
	private ControllerOnPc( ILed led, ISonar sonar,IRadarDisplay radar ) {
		this.led    = led;
		this.sonar  = sonar;
		this.radar  = radar;
	}
		
	public void start( ActionFunction endFun, int limit ) {
		this.endFun = endFun;
		ColorsOut.outappl("ControllerOnPc | start with endFun=" + endFun , ColorsOut.CYAN);
		sonar.activate();
		activate( limit );
	}
	

	/*
	 * Il ControllerOnPc riceve dati dal sonar e attiva gli use cases
	 */
	protected void activate( int limit ) {
  					boolean sonarActive = sonar.isActive();
					ColorsOut.outappl("ControllerOnPc | STARTS " + sonarActive , ColorsOut.CYAN);
					if( sonarActive ) {
						for( int i=1; i<=limit; i++) {
						//while( sonar.isActive() ) {
							IDistance d = sonar.getDistance();  
							ColorsOut.outappl("ControllerOnPc | d="+d +" i=" + i, ColorsOut.CYAN  );
							if( radar != null) RadarGuiUsecase.doUseCase( radar,d  );	//
	 						LedAlarmUsecase.doUseCase( led,  d  );  //Meglio inviare un msg su una coda
	 						BasicUtils.delay(DomainSystemConfig.sonarDelay);  //Al ritmo della generazione ...
	 					}
					}				
					//ColorsOut.outappl("ControllerOnPc | BYE", ColorsOut.CYAN  );
					sonar.deactivate();
					endFun.run("ControllerOnPc | BYE ");
					//System.exit(0);
	}
 }

package it.unibo.enablerCleanArch.domain;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.useCases.LedAlarmUsecase;
import it.unibo.enablerCleanArch.useCases.RadarGuiUsecase;

public class ControllerAsCoapSonarObserver  implements CoapHandler{
private String name;
private IRadarDisplay radar ;
private ILed led;
private int ampl;

	public ControllerAsCoapSonarObserver( String name,ILed led, IRadarDisplay radar, int ampl) {
		this.name  = name;
		this.radar = radar;
		this.led   = led;
		this.ampl  = ampl;
	}
	@Override
	public void onLoad(CoapResponse response) {
		String vs   = response.getResponseText();
		int v       = Integer.parseInt(vs) * ampl;
		IDistance d = new Distance(v);
 		Colors.outappl(name + " | " + vs, Colors.ANSI_PURPLE);
 		if( d.getVal() > 0 ) {
 			
			LedAlarmUsecase.doUseCase( led,  d  );   
	 		RadarGuiUsecase.doUseCase( radar,d  );	//
 		}
	}
	@Override
	public void onError() {
 		Colors.outerr(name + " | error"  );	
	}	
}
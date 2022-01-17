package it.unibo.enablerCleanArch.domain;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.useCases.LedAlarmUsecase;
import it.unibo.enablerCleanArch.useCases.RadarGuiUsecase;

public class ControllerAsCoapSonarObserver  implements CoapHandler{
private String name;
private IRadarDisplay radar ;
private ILed led;


	public ControllerAsCoapSonarObserver( String name,ILed led, IRadarDisplay radar) {
		this.name  = name;
		this.radar = radar;
		this.led   = led;
	}
	@Override
	public void onLoad(CoapResponse response) {
		String vs   = response.getResponseText();
		IDistance d = new Distance(vs);
 		Colors.outappl(name + " | " + vs, Colors.ANSI_PURPLE);
		LedAlarmUsecase.doUseCase( led,  d  );   
 		RadarGuiUsecase.doUseCase( radar,d  );	//
	}
	@Override
	public void onError() {
 		Colors.outerr(name + " | error"  );	
	}	
}
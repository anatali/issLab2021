package it.unibo.radarSystem22.actors.domain;

import it.unibo.kactor.ActorWrapper;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.interfaces.IRadarDisplay;
import it.unibo.radarSystem22.domain.RadarDisplay;
/*
 * Funge da interprete di 
 */
public class RadarDisplayActor extends ActorWrapper{
private IRadarDisplay radar ;

	public RadarDisplayActor(String name) {
		super(name);
		radar = RadarDisplay.getRadarDisplay();
	}

	@Override
	protected void doJob(IApplMessage arg0) {
		 
		
	}

 

}

package it.unibo.radarSystem22.actors.domain;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.interfaces.IRadarDisplay;
import it.unibo.radarSystem22.domain.RadarDisplay;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
/*
 * Funge da interprete di 
 */
public class RadarDisplayActor extends Actor22{
private IRadarDisplay radar ;

	public RadarDisplayActor(String name) {
		super(name);
		radar = RadarDisplay.getRadarDisplay();
	}

	@Override
	protected void doJob(IApplMessage msg) {
		BasicUtils.aboutThreads(getName()  + " |  Before doJob - ");
		ColorsOut.outappl( getName()  + " | doJob " + msg, ColorsOut.BLUE);
		radar.update(msg.msgContent(),"90");		
	}

}

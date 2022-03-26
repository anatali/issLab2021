package it.unibo.radarSystem22.actors.domain;

import it.unibo.actorComm.ActorJK;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.actors.businessLogic.BusinessLogicConfig;
import it.unibo.radarSystem22.actors.domain.support.DeviceLang;
import it.unibo.radarSystem22.actors.domain.support.DomainData;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.concrete.RadarDisplay;

/*
 * Funge da controller del Controller?
 * Potrebbe non esserci se il Controller usa un radar POJO
 */
public class RadarActor extends Actor22  {
private IRadarDisplay radar ;
private String angle = "60";
	public RadarActor(String name) {
		super(name);
		radar = RadarDisplay.getRadarDisplay();
	}

	@Override
	protected void doJob(IApplMessage msg) {
		//BasicUtils.aboutThreads(getName()  + " |  Before doJob - ");
		ColorsOut.outappl( getName()  + " | doJob " + msg, ColorsOut.YELLOW);
		radar.update(msg.msgContent(),angle);	
	}

 
}

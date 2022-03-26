package it.unibo.radarSystem22.actors.domain;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.actors.domain.support.DeviceLang;
import it.unibo.radarSystem22.actors.domain.support.DomainData;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.concrete.RadarDisplay;

/*
 * Funge da interprete di 
 */
public class RadarDisplayActor extends Actor22 implements IRadarDisplay{
private IRadarDisplay radar ;
private String angle = "90";
	public RadarDisplayActor(String name) {
		super(name);
		radar = RadarDisplay.getRadarDisplay();
	}

	@Override
	protected void doJob(IApplMessage msg) {
		//BasicUtils.aboutThreads(getName()  + " |  Before doJob - ");
		ColorsOut.out( getName()  + " | doJob " + msg, ColorsOut.BLUE);
		radar.update(msg.msgContent(),angle);		
	}

	@Override
	public void update(String d, String angle) {
		ColorsOut.out( getName()  + " | update distance " + d + " angle="+angle , ColorsOut.BLUE);
		this.angle = angle;  //SIDE EFFECT on POJO
		this.forwardToSelf(DeviceLang.cmd, ""+d);
		//IApplMessage updateRadarGui = MsgUtil.buildDispatch(getName(), DeviceLang.cmd, ""+d, DomainData.radarName);		
		//updateRadarGui : msg(cmd,dispatch,radarDisplay,radar,d,N)
		//Actor22.sendMsg(updateRadarGui, this );  
	}

	@Override
	public int getCurDistance() {
		return radar.getCurDistance();
	}

}

package it.unibo.radarSystem22.actors.domain;

import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.ActorWrapper;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;

public class ControllerWorker extends ActorWrapper{
private boolean goon = true;
private ActorBasic led;
private  ActorBasic sonar;

	public ControllerWorker(String name, ActorBasic led, ActorBasic sonar) {
		super(name);
		this.led   = led;
		this.sonar = sonar;
 	}

	@Override
	protected void doJob(IApplMessage msg) {
 		while( goon ) {
 			MsgUtil.sendMsg(DomainMsg.sonarDistance, sonar, null); 
 		}
		
	}

}

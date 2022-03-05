package it.unibo.radarSystem22.actors.domain;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.ActorWrapper;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.domain.utils.BasicUtils;

public class ControllerActor extends ActorWrapper{
private ActorBasic led;
private  ActorBasic sonar;

	public ControllerActor(String name, ActorBasic led, ActorBasic sonar) {
		super(name);
		this.led   = led;
		this.sonar = sonar;
	}

	@Override
	protected void doJob(IApplMessage msg) { 
		if( msg.isReply() ) {
			elabAnswer(msg);
		}else {
			String msgId = msg.msgId();
			switch( msgId ) {
				case DeviceLang.cmd    : elabCmd(msg);break;
 				default: ColorsOut.outerr(getName()  + " | unknown " + msgId);
			}		
		}
 	}
	
	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		//ColorsOut.outappl( getName()  + " | elabCmd " + msgCmd, ColorsOut.BLUE);
		switch( msgCmd ) {
			case "start" : {
				MsgUtil.sendMsg(DomainMsg.sonarActivate, sonar, null); //null è continuation.
				doControllerWork();
				break;
			}
			case "stop" : {
				MsgUtil.sendMsg(DomainMsg.sonarDeactivate, sonar, null); //null è continuation.
				break;
			}
		}		
	}
	
	protected void doControllerWork() {
		MsgUtil.sendMsg(DomainMsg.sonarDistance, sonar, null); //null è continuation.
		
	}
	
	
	
	protected void elabAnswer(IApplMessage msg) {
		String answer = msg.msgContent();
		ColorsOut.outappl( getName()  + " | elabAnswer " + answer, ColorsOut.BLUE);
 
		switch( answer ) {
			case "distance"  : {
				
				break;
			}
		}
	}

}

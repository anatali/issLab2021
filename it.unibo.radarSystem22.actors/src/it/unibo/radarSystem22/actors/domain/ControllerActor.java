package it.unibo.radarSystem22.actors.domain;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.ActorWrapper;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.actors.domain.support.DeviceLang;
import it.unibo.radarSystem22.actors.domain.support.DomainMsg;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public class ControllerActor extends ActorWrapper{
private ActorBasic led;
private ActorBasic sonar;

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
		ColorsOut.outappl( getName()  + " | elabCmd " + msgCmd, ColorsOut.GREEN);
		switch( msgCmd ) {
			case "activate" : {
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
		//Chiedo la distanza. Quando doJob riceve la risposta proseguo in elabAnswer
		MsgUtil.sendMsg(DomainMsg.sonarDistance, sonar, null); //null è continuation.		
	}
	
	
	
	protected void elabAnswer(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | elabAnswer " + msg, ColorsOut.GREEN);
		String answer = msg.msgContent();
		int d = Integer.parseInt(answer);
		if( d < DomainSystemConfig.DLIMIT ) {
			MsgUtil.sendMsg(DomainMsg.ledOn, led, null); //null è continuation.
		}else MsgUtil.sendMsg(DomainMsg.ledOff, led, null);
		BasicUtils.delay(DomainSystemConfig.sonarDelay*3);  //Intervengo ogni 3 dati generati
		if( d > DomainSystemConfig.DLIMIT - 10) {
			MsgUtil.sendMsg(DomainMsg.sonarDistance, sonar, null);
		}else {
			MsgUtil.sendMsg(DomainMsg.ledOff, led, null);
			MsgUtil.sendMsg(DomainMsg.sonarDeactivate, sonar, null); //null è continuation.
		}		
	}

}

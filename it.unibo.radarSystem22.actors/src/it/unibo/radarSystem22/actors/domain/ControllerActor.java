package it.unibo.radarSystem22.actors.domain;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.actors.domain.support.DeviceLang;
import it.unibo.radarSystem22.actors.domain.support.DomainMsg;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public class ControllerActor extends Actor22{
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
				Actor22.sendMsg(DomainMsg.sonarActivate, sonar );  
				doControllerWork();
				break;
			}
			case "stop" : {
				Actor22.sendMsg(DomainMsg.sonarDeactivate, sonar );  
				break;
			}
		}		
	}
	
	protected void doControllerWork() {
		//Chiedo la distanza. Quando doJob riceve la risposta proseguo in elabAnswer
		Actor22.sendMsg(DomainMsg.sonarDistance, sonar );  		
	}
	
	
	
	protected void elabAnswer(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | elabAnswer " + msg, ColorsOut.GREEN);
		String answer = msg.msgContent();
		int d = Integer.parseInt(answer);
		if( d < DomainSystemConfig.DLIMIT ) {
			Actor22.sendMsg(DomainMsg.ledOn, led );  
		}else Actor22.sendMsg(DomainMsg.ledOff, led );
		BasicUtils.delay(DomainSystemConfig.sonarDelay*3);  //Intervengo ogni 3 dati generati
		if( d > DomainSystemConfig.DLIMIT - 10) {
			Actor22.sendMsg(DomainMsg.sonarDistance, sonar );
		}else {
			Actor22.sendMsg(DomainMsg.ledOff, led );
			Actor22.sendMsg(DomainMsg.sonarDeactivate, sonar );  
		}		
	}

}

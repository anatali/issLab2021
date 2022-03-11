package it.unibo.radarSystem22.actors.businessLogic;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.actors.domain.support.DeviceLang;
import it.unibo.radarSystem22.actors.domain.support.DomainData;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;


/*
 * Controller che interagisce con dispositivi-actors
 */
public class ControllerActor extends Actor22{
private ActorBasic led;
private ActorBasic sonar;
private ActorBasic radar;

/*
 * Costruttore che riceve i riferimenti agli attori componenti il sistema
 */
	public ControllerActor(String name, ActorBasic led, ActorBasic sonar, ActorBasic radar) {
		super(name);
		this.led   = led;
		this.sonar = sonar;
		this.radar = radar;
	}
/*
* Costruttore che riceve i NOMI degli attori componenti  
*/
	public ControllerActor(String name, String ledName, String sonarName, String radarName) {
		this( name, Actor22.getActor(ledName), Actor22.getActor(sonarName),Actor22.getActor(radarName) );
//		ActorBasic a = Actor22.getActor(ledName);
//		if( a != null ) this.led = a;
//		else ColorsOut.outerr("NOT FOUND Actor with name " + ledName );
//		a = Actor22.getActor(sonarName);
//		if( a != null ) this.sonar = a;
//		else ColorsOut.outerr("NOT FOUND Actor with name " + sonarName );
//		a = Actor22.getActor(radarName);
//		if( a != null ) this.radar = a;
//		else ColorsOut.outerr("NOT FOUND Actor with name " + radarName );		
	}

	@Override
	protected void doJob(IApplMessage msg) { 
		if( msg.isReply() ) {
			elabAnswer(msg);
		}else {
			String msgId = msg.msgId();
			switch( msgId ) {
				case DeviceLang.cmd  : elabCmd(msg);break;
 				default: ColorsOut.outerr(getName()  + " | unknown " + msgId);
			}		
		}
 	}
	
	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		ColorsOut.outappl( getName()  + " | elabCmd " + msgCmd, ColorsOut.GREEN);
		switch( msgCmd ) {
			case "activate" : {
				Actor22.sendMsg(DomainData.sonarActivate, sonar );
				doControllerWork();
				break;
			}
			case "stop" : {
				Actor22.sendMsg(DomainData.sonarDeactivate, sonar );  
				break;
			}
		}		
	}
	
	protected void doControllerWork() {
		//Chiedo la distanza. Quando doJob riceve la risposta proseguo in elabAnswer
		Actor22.sendMsg(DomainData.sonarDistance, sonar );  		
	}
	
	
	
	protected void elabAnswer(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | elabAnswer " + msg, ColorsOut.GREEN);
		//SONAR DATA
		String answer = msg.msgContent();
		int d = Integer.parseInt(answer);
		//RADAR
		if( radar != null ) {
			IApplMessage updateRadarGui = MsgUtil.buildDispatch(getName(), DeviceLang.cmd, ""+d, DomainData.radarName);		
			Actor22.sendMsg(updateRadarGui, radar );  
		}
		//LED
		if( d < DomainData.DLIMIT ) {
			Actor22.sendMsg(DomainData.ledOn, led );  
		}else Actor22.sendMsg(DomainData.ledOff, led );
		BasicUtils.delay(DomainSystemConfig.sonarDelay*3);  //Intervengo ogni 3 dati generati
		//CONTROL
		if( d > DomainData.DLIMIT - 10) {
			Actor22.sendMsg(DomainData.sonarDistance, sonar );
		}else {
			Actor22.sendMsg(DomainData.ledOff, led );
			Actor22.sendMsg(DomainData.sonarDeactivate, sonar );  
		}		
	}

}

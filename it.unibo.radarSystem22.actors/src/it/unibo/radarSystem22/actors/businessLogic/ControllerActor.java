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
		ColorsOut.outappl( getName()  + " | elabCmd=" + msgCmd, ColorsOut.GREEN);
		switch( msgCmd ) {
			case "activate" : {
				//sendMsg(DomainData.sonarActivate, sonar );
				Actor22.sendAMsg( DomainData.sonarActivate, sonar);
				doControllerWork();
				break;
			}
			case "stop" : {
				//sendMsg(DomainData.sonarDeactivate, sonar );  
				Actor22.sendAMsg( DomainData.sonarDeactivate, sonar);
				break;
			}
		}		
	}
	
	protected void doControllerWork() {
		//Chiedo la distanza. Quando doJob riceve la risposta proseguo in elabAnswer
		//sendMsg( DomainData.sonarDistance, sonar );  
		ColorsOut.outappl( getName()  + " | doControllerWork "  , ColorsOut.GREEN);
		//sendMsg(DomainData.sonarDistance, sonar);
		Actor22.sendAMsg( DomainData.sonarDistance, sonar);
	}
	
	
	
	protected void elabAnswer(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | elabAnswer " + msg, ColorsOut.GREEN);
		//SONAR DATA
		String answer = msg.msgContent();
		int d = Integer.parseInt(answer);
		//RADAR
		if( radar != null ) {
			IApplMessage updateRadarGui = MsgUtil.buildDispatch(getName(), DeviceLang.cmd, ""+d, DomainData.radarName);		
			//sendMsg(updateRadarGui, radar );  
			Actor22.sendAMsg( updateRadarGui, radar);
		}
		//LED
		if( d < DomainData.DLIMIT ) {
			//sendMsg(DomainData.ledOn, led );  
			Actor22.sendAMsg( DomainData.ledOn, led );
		}else {
			//sendMsg(DomainData.ledOff, led );
			Actor22.sendAMsg( DomainData.ledOff, led );
		}
		BasicUtils.delay(DomainSystemConfig.sonarDelay*3);  //Intervengo ogni 3 dati generati
		//CONTROL
		if( d > DomainData.DLIMIT - 10) {
			//sendMsg(DomainData.sonarDistance, sonar );
			Actor22.sendAMsg( DomainData.sonarDistance, sonar);
		}else {
			//sendMsg(DomainData.ledOff, led );
			//sendMsg(DomainData.sonarDeactivate, sonar );  
			Actor22.sendAMsg( DomainData.ledOff, led );
			Actor22.sendAMsg( DomainData.sonarDeactivate, sonar);
		}		
	}

}

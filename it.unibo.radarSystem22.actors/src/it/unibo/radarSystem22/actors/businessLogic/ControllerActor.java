package it.unibo.radarSystem22.actors.businessLogic;

import it.unibo.actorComm.ActorJK;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.actors.domain.support.DeviceLang;
import it.unibo.radarSystem22.actors.domain.support.DomainData;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;


/*
 * Controller ACTOR che interagisce con dispositivi-ACTOR
 * Il controller viene attivato dal main (UsingActorsOnPc)
 * Il Controller deve innviare info al main?
 * Deve esistere un Controller del Controller?
 */
public class ControllerActor extends Actor22{
	
private int curDistance = 1000;

  	public ControllerActor(String name ) {
		super(name);
 	}
 

	@Override
	protected void doJob(IApplMessage msg) { 
		//ColorsOut.outappl( getName()  + " | doJob=" + msg, ColorsOut.GREEN);
		if( msg.isReply() ) {
			elabAnswer(msg);  //elabora answer di sonarcaller
		}else { 
			elabCmd(msg);
		}
		
 	}
	
	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		switch( msgCmd ) {
			case "activate" : {
 				ActorJK.sendAMsg( DomainData.sonarActivate );
 				new SonarCallerActor( DomainData.sonarCallerName );
 				ActorJK.sendAMsg( DomainData.sonarCallerActivate );  
 				break;
			}
			case "stop" : {
 				ActorJK.sendAMsg( DomainData.sonarDeactivate );
				break;
			}
		}		
	}
	
	
	protected void elabAnswer(IApplMessage msg) {
 		//SONAR DATA
		String answer = msg.msgContent();
		curDistance   = Integer.parseInt(answer);
		ColorsOut.outappl( getName()  + " | elabAnswer =" + curDistance, ColorsOut.GREEN);
		//RADAR
 		IApplMessage updateRadarGui = MsgUtil.buildDispatch(getName(), DeviceLang.cmd, ""+curDistance, DomainData.radarName);		
  		ActorJK.sendAMsg( updateRadarGui );
 		//LED
		if( curDistance < BusinessLogicConfig.DLIMIT ) {
 			ActorJK.sendAMsg( DomainData.ledOn  );
		}else {
 			ActorJK.sendAMsg( DomainData.ledOff  );
		}
		//CONTROLLER GOON or TERMINATION. Se richiamo Controller NON TERMINA
		if( curDistance > BusinessLogicConfig.DLIMIT - 10 ) {
			CommUtils.delay(DomainSystemConfig.sonarDelay*3);  //Intervengo ogni 3 dati generati
			ActorJK.sendAMsg( DomainData.sonarCallerActivate );
		}else {
 			ActorJK.sendAMsg( DomainData.ledOff  );
			ActorJK.sendAMsg( DomainData.sonarDeactivate );
		}		
	}

}

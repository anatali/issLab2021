package it.unibo.radarSystem22.actors.experiment22;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.actors.domain.main.RadarSystemConfig;
import it.unibo.radarSystem22.actors.domain.support.DeviceLang;
import it.unibo.radarSystem22.actors.domain.support.DomainData;


/*
 * Controller  MESSAGE-DRIVEN
 *   1) attende l'autoforward di activate
 *   2) activate -> attiva il sonar e invia richiesta di distanza
 *   3) reply    -> invia dispatch al radar e al led
 *                  se la distanza è > DLIMIT invia nuova richiesta di distanza
 *                  altrimenti si ferma
 */
public class ControllerActor22 extends Actor22{  
	
 	public ControllerActor22( String name ) {
 		super(name);
//		IApplMessage controllerActivate   = 
//				MsgUtil.buildDispatch("main", DeviceLang.cmd, "activate", name);
		//Actor22.sendMsg( controllerActivate, this);
 		forwardToSelf( DeviceLang.cmd, "activate" );   
 	}
 	
	@Override 
	protected void doJob(IApplMessage msg) { 
		ColorsOut.outappl(getName()  + " | RECEIVES " + msg, ColorsOut.GREEN);
		if( msg.isReply() ) {
			elabAnswer(msg);
			return;
		}
 		String msgId = msg.msgId();
		switch( msgId ) {
			case DeviceLang.cmd     : elabCmd(msg);break;
			case "distance"         : elabAnswer(msg);break;	//ABUSO ...
  		    default: ColorsOut.outerr(getName()  + " | unknown " + msgId);
		}		
  	}
	
	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		switch( msgCmd ) {
			case "activate" : {
				doControlActivate();
 				break;
			}
 		}		
	}

	protected void elabAnswer(IApplMessage msg) {
		ColorsOut.outappl(getName()  + " | elabAnswer " + msg, ColorsOut.GREEN);
 		String msgId = msg.msgId();
		switch( msgId ) {
			case "distance" : elabDistance(msg.msgContent());break;
  		    default: ColorsOut.outerr(getName()  + " | unknown " + msgId);
		}		
	}
	
	
	protected void elabDistance(String ds) {
		int d = Integer.parseInt( ds );
		if( RadarSystemConfig.withRadarGui ) forward( DeviceLang.cmd, ds, "radar" );
		if( d < RadarSystemConfig.DLIMIT ) {
			forward( DeviceLang.cmd, "turnOn", "led" );
		}else {
			forward( DeviceLang.cmd, "turnOff", "led" );	
			request( DeviceLang.req, "distance", "sonar" );
		}
		
	}
	protected void doControlActivate() {
		ColorsOut.outappl( getName()  + " | doControlActivate ", ColorsOut.GREEN);
 		forward( DeviceLang.cmd, "activate", "sonar" );
  		request( DeviceLang.req, "distance", "sonar" );
	}
 	
	
	
 
}

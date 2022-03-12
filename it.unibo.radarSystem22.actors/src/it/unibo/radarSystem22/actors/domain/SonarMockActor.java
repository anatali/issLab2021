package it.unibo.radarSystem22.actors.domain;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.interfaces.ISonar;

/*
 * L'invio di risposta a un actore remoto deve essere fatto da MsgHandlerForActor
 */
public class SonarMockActor extends Actor22{
private ISonar sonar;

	public SonarMockActor(String name) {
		super(name);
		sonar = DeviceFactory.createSonar();
	}

	@Override
	protected void doJob(IApplMessage msg) {
		BasicUtils.aboutThreads(getName()  + " |  Before doJob - ");
		//ColorsOut.outappl( getName()  + " | doJob " + msg, ColorsOut.BLUE);
		if( msg.isRequest() ) elabRequest(msg);
		else if( msg.isDispatch() ) elabCmd(msg);
		else ColorsOut.outerr(getName()  + " | unknown " + msg.msgId());
	}

	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		//ColorsOut.outappl( getName()  + " | elabCmd " + msgCmd, ColorsOut.BLUE); 
		switch( msgCmd ) {
			case "activate"    : sonar.activate();break;
			case "deactivate"  : sonar.deactivate();break;
 			default: ColorsOut.outerr(getName()  + " | unknown " + msgCmd);
		}
	}

	protected void elabRequest(IApplMessage msg) {
		String msgReq = msg.msgContent();
		//ColorsOut.outappl( getName()  + " | elabRequest " + msgCmd, ColorsOut.BLUE);
		switch( msgReq ) {
			case "isActive"  :{
				boolean b = sonar.isActive();
				IApplMessage reply = MsgUtil.buildReply(getName(), "sonarState", ""+b, msg.msgSender());
				sendAnswer(msg,reply);	//in Actor22	
				break;
			}
			case "distance"  :{
				int d = sonar.getDistance().getVal();
				IApplMessage reply = MsgUtil.buildReply(getName(), "distance", ""+d, msg.msgSender());
				sendAnswer(msg,reply);	//in Actor22		
				break;
			}
 			default: ColorsOut.outerr(getName()  + " | elabRequest unknown " + msgReq);
		}
	}
	

}

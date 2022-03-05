package it.unibo.radarSystem22.actors.domain;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.ActorWrapper;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.interfaces.ISonar;

/*
 * ActorWrapper senza contesto non è in grado di inviare risposte
 */
public class SonarMockActor extends ActorWrapper{
private ISonar sonar;

	public SonarMockActor(String name) {
		super(name);
		sonar = DeviceFactory.createSonar();
	}

	@Override
	protected void doJob(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | doJob " + msg, ColorsOut.BLUE);
		String msgId = msg.msgId();
		switch( msgId ) {
			case DeviceLang.cmd : elabCmd(msg);break;
			case DeviceLang.req : elabRequest(msg);break;
			default: ColorsOut.outerr(getName()  + " | unknown " + msgId);
		}		
	}

	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		//ColorsOut.outappl( getName()  + " | elabCmd " + msgCmd, ColorsOut.BLUE); 
		switch( msgCmd ) {
			case "activate"  : sonar.activate();break;
			case "deactivate"  : sonar.deactivate();break;
 			default: ColorsOut.outerr(getName()  + " | unknown " + msgCmd);
		}
	}

	protected void elabRequest(IApplMessage msg) {
		String msgReq = msg.msgContent();
		//ColorsOut.outappl( getName()  + " | elabRequest " + msgCmd, ColorsOut.BLUE);
		switch( msgReq ) {
			case "getState"  :{
				boolean b = sonar.isActive();
				IApplMessage reply = MsgUtil.buildReply(getName(), "sonarState", ""+b, msg.msgSender());
				ColorsOut.outappl( getName()  + " | reply= " + reply, ColorsOut.BLUE);				
				break;
			}
			case "distance"  :{
				int d = sonar.getDistance().getVal();
				IApplMessage reply = MsgUtil.buildReply(getName(), "distance", ""+d, msg.msgSender());
				ColorsOut.outappl( getName()  + " | reply= " + reply, ColorsOut.BLUE);			
				//MsgUtil.sendMsg(reply, getContext(), null);
				break;
			}
 			default: ColorsOut.outerr(getName()  + " | unknown " + msgReq);
		}
	}
}

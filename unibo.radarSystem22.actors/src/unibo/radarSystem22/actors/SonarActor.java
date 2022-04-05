package unibo.radarSystem22.actors;

import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.domain.DeviceFactory;
import unibo.actor22.QakActor22;
import it.unibo.radarSystem22.domain.interfaces.*;
import unibo.actor22comm.utils.ColorsOut;

/*
 * L'invio di risposta a un actore remoto deve essere fatto da MsgHandlerForActor
 */
public class SonarActor extends QakActor22{
private ISonar sonar;

	public SonarActor(String name) {
		super(name);
		sonar = DeviceFactory.createSonar();
	}

	@Override
	protected void handleMsg(IApplMessage msg) {
		//CommUtils.aboutThreads(getName()  + " |  Before doJob - ");
		//ColorsOut.outappl( getName()  + " | doJob " + msg, ColorsOut.BLUE);
		if( msg.isRequest() ) elabRequest(msg);
		else elabCmd(msg);
//		else ColorsOut.outerr(getName()  + " | unknown " + msg.msgId());
	}

	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		//ColorsOut.outappl( getName()  + " | elabCmd=" + msgCmd, ColorsOut.CYAN); 
		switch( msgCmd ) {
			case "activate"    : sonar.activate();break;
			case "deactivate"  : sonar.deactivate();break;
 			default: ColorsOut.outerr(getName()  + " | unknown " + msgCmd);
		}
	}

	protected void elabRequest(IApplMessage msg) {
		String msgReq = msg.msgContent();
		//ColorsOut.outappl( getName()  + " | elabRequest " + msg, ColorsOut.CYAN);
		switch( msgReq ) {
			case "isActive"  :{
				boolean b = sonar.isActive();
				IApplMessage reply = MsgUtil.buildReply(getName(), "sonarState", ""+b, msg.msgSender());
				//ColorsOut.outappl( getName()  + " | sendAnswer reply=" + reply, ColorsOut.CYAN);
 				sendReply( msg,reply );
				break;
			}
			case "getDistance"  :{
				int d = sonar.getDistance().getVal();
				IApplMessage reply = MsgUtil.buildReply(getName(), "distance", ""+d, msg.msgSender()); //"distance"
				//ColorsOut.outappl( getName()  + " | sendAnswer reply=" + reply, ColorsOut.CYAN);
				sendReply( msg,reply );
				break;
			}
 			default: ColorsOut.outerr(getName()  + " | elabRequest unknown " + msgReq);
		}
	}
	

}
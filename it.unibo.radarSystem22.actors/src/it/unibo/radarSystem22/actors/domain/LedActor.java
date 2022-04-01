package it.unibo.radarSystem22.actors.domain;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.actors.simple.main.ApplData;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;


/*
 * Funge da interprete di 
 */
public class LedActor extends Actor22{
private ILed led;

	public LedActor(String name) {
		super(name);
		led = DeviceFactory.createLed();
	}

	@Override
	protected void doJob(IApplMessage msg) {
		BasicUtils.aboutThreads(getName()  + " |  Before doJob - ");
		ColorsOut.outappl( getName()  + " | doJob " + msg, ColorsOut.CYAN);
		if( msg.isRequest() ) elabRequest(msg);
		else elabCmd(msg);
	}

	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
 		switch( msgCmd ) {
			case ApplData.comdLedon  : led.turnOn();break;
			case ApplData.comdLedoff : led.turnOff();break;
			default: ColorsOut.outerr(getName()  + " | unknown " + msgCmd);
		}
	}
 
	protected void elabRequest(IApplMessage msg) {
		String msgReq = msg.msgContent();
		//ColorsOut.outappl( getName()  + " | elabRequest " + msgCmd, ColorsOut.BLUE);
 
		switch( msgReq ) {
			case ApplData.reqLedState  :{
				boolean b = led.getState();
				IApplMessage reply = MsgUtil.buildReply(getName(), ApplData.reqLedState, ""+b, msg.msgSender());
				//ColorsOut.outappl( getName()  + " | reply= " + reply, ColorsOut.CYAN);
 				Actor22.sendReply(msg, reply );				
				break;
			}
 			default: ColorsOut.outerr(getName()  + " | unknown " + msgReq);
		}
	}

}

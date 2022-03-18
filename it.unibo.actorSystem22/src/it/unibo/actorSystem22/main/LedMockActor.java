package it.unibo.actorSystem22.main;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;


/*
 *  
 */
public class LedMockActor extends Actor22{
private ILed led;

	public LedMockActor(String name) {
		super(name);
		led = DeviceFactory.createLed();
	}

	@Override
	protected void doJob(IApplMessage msg) {
		BasicUtils.aboutThreads(getName()  + " |  Before doJob - ");
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
			case "turnOn"  : led.turnOn();break;
			case "turnOff" : led.turnOff();break;
			default: ColorsOut.outerr(getName()  + " | unknown " + msgCmd);
		}
	}
 
	protected void elabRequest(IApplMessage msg) {
		String msgReq = msg.msgContent();
		//ColorsOut.outappl( getName()  + " | elabRequest " + msgCmd, ColorsOut.BLUE);
 
		switch( msgReq ) {
			case "getState"  :{
				boolean b = led.getState();
				IApplMessage reply = MsgUtil.buildReply(getName(), "ledState", ""+b, msg.msgSender());
				ColorsOut.outappl( getName()  + " | reply= " + reply, ColorsOut.BLUE);
				
				break;
			}
 			default: ColorsOut.outerr(getName()  + " | unknown " + msgReq);
		}
	}

}

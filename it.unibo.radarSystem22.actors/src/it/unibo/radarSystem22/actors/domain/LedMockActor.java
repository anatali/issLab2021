package it.unibo.radarSystem22.actors.domain;

import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.actors.domain.support.DeviceLang;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22.domain.utils.BasicUtils;


/*
 * Funge da interprete di 
 */
public class LedMockActor extends Actor22{
private ILed led;

	public LedMockActor(String name) {
		super(name);
		led = DeviceFactory.createLed();
	}

	@Override
	protected void doJob(IApplMessage msg) {
		//BasicUtils.aboutThreads(getName()  + " |  Before doJob - ");
		ColorsOut.outappl( getName()  + " | doJob " + msg, ColorsOut.BLUE);
		String msgId = msg.msgId();
		if( msg.isRequest() ) elabRequest(msg);
		else elabCmd(msg);
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
				//WARNING: il caller non è locale => Actor22.getActor fails!
				//Invio reply al nodo PC. Come sfrutto le connessioni? 
				//Ctx di Actor22 può tenere traccia delle richieste
				//ActorBasic ar = Actor22.getActor("ar"+msg.msgSender()) ;
				Actor22.sendReply(msg, reply );
				
				break;
			}
 			default: ColorsOut.outerr(getName()  + " | unknown " + msgReq);
		}
	}

}

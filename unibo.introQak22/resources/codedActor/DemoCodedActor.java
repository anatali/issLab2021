package codedActor;

import it.unibo.kactor.*;
import unibo.actor22.QakActor22;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;


public class DemoCodedActor extends QakActor22{
 

	public DemoCodedActor(String name) {
		super(name);
 	}

	@Override
	protected void handleMsg(IApplMessage msg) {
		CommUtils.aboutThreads(getName()  + " |  Before doJob - ");
		//ColorsOut.outappl( getName()  + " | doJob " + msg, ColorsOut.BLUE);
		if( msg.isRequest() ) elabRequest(msg);
		else if( msg.isDispatch() ) elabCmd(msg);
		else ColorsOut.outerr(getName()  + " | unknown " + msg.msgId());
	}

	protected void elabCmd(IApplMessage msg) {
		String msgCmd = msg.msgContent();
		ColorsOut.outappl( getName()  + " | elabCmd=" + msgCmd, ColorsOut.CYAN); 
	}

	protected void elabRequest(IApplMessage msg) {
		String msgReq = msg.msgContent();
		ColorsOut.outappl( getName()  + " | elabRequest " + msg, ColorsOut.CYAN);
	}

}

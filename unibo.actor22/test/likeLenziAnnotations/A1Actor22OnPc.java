package likeLenziAnnotations;

import it.unibo.kactor.IApplMessage;
import unibo.actor22.QakActor22;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;

public class A1Actor22OnPc extends QakActor22{

	public A1Actor22OnPc(String name) {
		super(name);
 	}
 
	@Override
	protected void handleMsg(IApplMessage msg) {
		if( msg.isDispatch() && msg.msgId().equals(SystemData.activateActorCmd) ) {
			CommUtils.delay(1000);
			ColorsOut.outappl( getName()  + " | ACTIVATED " , ColorsOut.YELLOW);
			forward( SystemData.startSysCmd(getName(),"a2") );
		}else {
 			elabMsg(msg);
		}	
	}

	protected void elabMsg(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | elabMsg " + msg, ColorsOut.CYAN);	
		
	}

}

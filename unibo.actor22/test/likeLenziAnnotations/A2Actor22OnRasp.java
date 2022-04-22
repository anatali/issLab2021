package likeLenziAnnotations;

import it.unibo.kactor.IApplMessage;
import unibo.actor22.QakActor22;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;

public class A2Actor22OnRasp extends QakActor22{

	public A2Actor22OnRasp(String name) {
		super(name);
 	}

	@Override
	protected void handleMsg(IApplMessage msg) {
		if( msg.isDispatch() && msg.msgId().equals(SystemData.activateActorCmd) ) {
	 		ColorsOut.outappl( getName()  + " | ACTIVATED " , ColorsOut.YELLOW);
	 		CommUtils.delay(1000);  //a3Rasp should be not activated ...
	 		forward( SystemData.demoSysCmd(getName(),"a3Rasp") );
		}else {
	 		elabMsg(msg);
		}	
	}
	protected void elabMsg(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | elabMsgggggggggggggg " + msg, ColorsOut.CYAN);	
		CommUtils.delay(1000);
 		request( SystemData.demoSysRequest(getName(),"a3Pc") );
		if( msg.isRequest() ) {
			
		}
	}

}

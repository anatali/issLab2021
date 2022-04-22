package likeLenziAnnotations;

import it.unibo.kactor.IApplMessage;
import unibo.actor22.QakActor22;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.utils.ColorsOut;

public class DemoActor22OnRaspi extends QakActor22{

	public DemoActor22OnRaspi(String name) {
		super(name);
 	}

	@Override
	protected void handleMsg(IApplMessage msg) {
		if( msg.isDispatch() && msg.msgId().equals(SystemData.activateActorCmd) ) {
	 		ColorsOut.out( getName()  + " | ACTIVATED " , ColorsOut.YELLOW);
		}else {
	 		elabMsg(msg);
		}	
	}
	protected void elabMsg(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | elabMsg " + msg, ColorsOut.CYAN);	
		if( msg.isRequest() ) {
			
		}
	}

}

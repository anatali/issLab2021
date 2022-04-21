package likeLenziAnnotations;

import it.unibo.kactor.IApplMessage;
import unibo.actor22.QakActor22;
import unibo.actor22comm.utils.ColorsOut;

public class DemoActor22 extends QakActor22{

	public DemoActor22(String name) {
		super(name);
 	}

	@Override
	protected void handleMsg(IApplMessage msg) {
		ColorsOut.outappl( getName()  + " | handleMsg " + msg, ColorsOut.CYAN);	
		
	}

}

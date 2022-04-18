package unibo.wenvUsage22.annot;

import it.unibo.kactor.IApplMessage;
import unibo.actor22comm.utils.ColorsOut;
import unibo.wenvUsage22.common.ApplData;

public class FirstAnnotated extends QakActor2FsmAnnot{

	public FirstAnnotated(String name) {
		super(name);
 	}
	
	@StateSpec( name = "s0")
	@StateTransitionSpec( state = {"s0", "s1"}, 
	                      msgId = {ApplData.moveCmdId, "halt"})
	protected void s0( IApplMessage msg ) {
		ColorsOut.outappl( getName()  + " | s0 " + msg, ColorsOut.MAGENTA);	
		outInfo(""+msg);
	}
	@StateSpec( name = "s1")
	protected void s1( IApplMessage msg ) {
		ColorsOut.outappl( getName()  + " | s1 " + msg, ColorsOut.MAGENTA);	
		outInfo(""+msg);
	}

	@Override
	protected void setTheInitialState() {
		declareAsInitialState( "s0" );
	}
	

}

package unibo.wenvUsage22.annot;

import it.unibo.kactor.IApplMessage;
import unibo.actor22comm.utils.ColorsOut;
import unibo.wenvUsage22.actors.QakActor22FsmAnnot;
import unibo.wenvUsage22.common.ApplData;

/*
 * FSM che riceve messaggi da MainFirstAnnotated
 */
public class FirstAnnotated extends QakActor22FsmAnnot{

	public FirstAnnotated(String name) {
		super(name);
 	}
	
	@State( name = "s0", initial=true)
//	@Transition( state = {"s0", "s1"}, 
//	                      msgId = {ApplData.moveCmdId, "halt"})
	@Transition( state = "s0", msgId=ApplData.moveCmdId )
	@Transition( state = "s1", msgId = "halt" )
	protected void s0( IApplMessage msg ) {
		//ColorsOut.outappl( getName()  + " | s0 " + msg, ColorsOut.BLUE);	
		outInfo(""+msg);
	}
	
	@State( name = "s1")
	protected void s1( IApplMessage msg ) {
		//ColorsOut.outappl( getName()  + " | s1 " + msg, ColorsOut.BLUE);	
		outInfo(""+msg);
	}

//	@Override
//	public void handleAsObserver(String data) {
//		outInfo(""+data);		
//	}

}

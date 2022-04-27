package unibo.actor22.guards;

import it.unibo.kactor.IApplMessage;
import unibo.actor22.QakActor22FsmAnnot;
import unibo.actor22.annotations.*;
import unibo.actor22comm.SystemData;

public class TestGuards extends QakActor22FsmAnnot{
private int n = 0;

	public TestGuards(String name) {
		super(name);
 	}
 	
	@State( name = "s0", initial=true)
	@Transition( state = "s1" ,  msgId = SystemData.demoSysId, guard = Guard0.class )
	//@Transition( state = "alarm" ,        msgId = SystemData.fireEventId )
	//@Transition( state = "wallDetected" , msgId = SystemData.endMoveKoId )
	protected void s0( IApplMessage msg ) {
		outInfo(""+msg );
		Guard0.setValue(n);
		this.autoMsg( SystemData.demoSysCmd( getName(),getName() ) );
	}
 
	@State( name = "s1" )
	protected void s1( IApplMessage msg ) {
		outInfo(""+msg );
	}


}

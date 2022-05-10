package unibo.basicRobot22;

import it.unibo.kactor.IApplMessage;
import unibo.actor22.QakActor22FsmAnnot;
import unibo.actor22.annotations.State;
import unibo.actor22.annotations.Transition;
import unibo.actor22.annotations.TransitionGuard;

public class RobotCmdSender extends QakActor22FsmAnnot{
protected IApplMessage requestW ;
protected IApplMessage requestA ;
protected int NumStep   = 1;

	public RobotCmdSender(String name) {
		super(name);
 	}
	
	@State( name = "activate", initial=true)
	@Transition( state = "start"  )   
	protected void activate( IApplMessage msg ) {
		outInfo(""+msg);
   	}
	
	@State( name = "start" )
	@Transition( state = "work"   )
	protected void start( IApplMessage msg ) {
		requestW = ApplData.w(getName(), MainBasicRobot.robotName);
		requestA = ApplData.a(getName(), MainBasicRobot.robotName);
		outInfo(""+msg);  
	}

	@State( name = "work" )
	@Transition( state = "work",         msgId = "endMoveOk"   )
	@Transition( state = "collision",    msgId = "endMoveKo"   )
	protected void work( IApplMessage msg ) {
		outInfo(""+msg);  
		request( requestW );
	}
	@State( name = "collision" )
	@Transition( state = "work", msgId = "endMoveOk", guard="roundNotCompleted"   )
	protected void collision( IApplMessage msg ) {
		outInfo(""+msg);  
		NumStep++;
		request( requestA );
	}	
	
	
	@TransitionGuard
	protected boolean roundNotCompleted() {
		outInfo( "roundNotCompleted  " + (NumStep ) );
		return NumStep < 5;
	}	
	@TransitionGuard
	protected boolean roundCompleted() {
		outInfo( "roundCompleted  " + (NumStep ) );
		return NumStep == 5;
	}		
}

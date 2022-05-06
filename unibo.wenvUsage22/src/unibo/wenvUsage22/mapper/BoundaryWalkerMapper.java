package unibo.wenvUsage22.mapper;

import it.unibo.kactor.IApplMessage;
import unibo.actor22.QakActor22FsmAnnot;
import unibo.actor22.annotations.State;
import unibo.actor22.annotations.Transition;
import unibo.actor22.annotations.TransitionGuard;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.ws.WsConnection;
import unibo.wenvUsage22.common.VRobotMoves;
import unibo.wenvUsage22.basicRobot.prototype0.WsConnApplObserver;

public class BoundaryWalkerMapper extends QakActor22FsmAnnot  {
	private Interaction2021 conn;	
 	private int ncorner  = 0;

 	public BoundaryWalkerMapper(String name) {
		super(name);
 	}
	
	@State( name = "robotStart", initial=true)
	@Transition( state = "robotMoving" ,  msgId = SystemData.endMoveOkId )
	@Transition( state = "wallDetected" , msgId = SystemData.endMoveKoId )
	protected void robotStart( IApplMessage msg ) {
		outInfo(""+msg + " connecting (blocking all the actors ) ... ");	
 		conn = WsConnection.create("localhost:8091" ); 	 
 		outInfo("connected "+conn);	
   		((WsConnection)conn).addObserver( new WsConnApplObserver(getName(), true) );
  		VRobotMoves.step(getName(),conn);
	}
	
 	@State( name = "robotMoving" )
 	@Transition( state = "robotMoving" ,  msgId = SystemData.endMoveOkId)
  	@Transition( state = "wallDetected" , msgId = SystemData.endMoveKoId )
	protected void robotMoving( IApplMessage msg ) {
		//outInfo(""+msg);	
		VRobotMoves.step(getName(),conn);
 	}
 	
 	@State( name = "wallDetected" )
	@Transition( state = "robotMoving" , 
		msgId = SystemData.endMoveOkId,guard="notCompleted"   )
 	@Transition( state = "endWork" ,     
 		msgId = SystemData.endMoveOkId,guard="completed" )
	protected void wallDetected( IApplMessage msg ) {
		outInfo("ncorner="+ ncorner + " " + msg);	
		ncorner++;
 		VRobotMoves.turnLeft(getName(), conn);
 	}


 	
 	@State( name = "endWork" )
 	protected void endWork( IApplMessage msg ) {
 		//VRobotMoves.turnLeft(getName(), conn);
		outInfo("BYE" );	
 		System.exit(0);
 	}

//----------------------------------------------
	@TransitionGuard
	protected boolean completed() {
		outInfo("completed "+ (ncorner ) );
		return ncorner == 4 ;
	}
	@TransitionGuard
	protected boolean notCompleted() {
		outInfo("notCompleted "+ (ncorner ) );
		return ncorner < 4 ;
	}	
}

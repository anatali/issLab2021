package unibo.wenvUsage22.annot.walker.alarms;

import it.unibo.kactor.IApplMessage;
import unibo.actor22.Qak22Context;
import unibo.actor22.QakActor22FsmAnnot;
import unibo.actor22.annotations.State;
import unibo.actor22.annotations.Transition;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.ws.WsConnection;
import unibo.wenvUsage22.annot.walker.WsConnWEnvObserver;
import unibo.wenvUsage22.common.VRobotMoves;

public class BoundaryWalkerAnnotAlarms extends QakActor22FsmAnnot  {
	private Interaction2021 conn;	
 	private int ncorner  = 0;
	
	public BoundaryWalkerAnnotAlarms(String name) {
		super(name);
		//EventHandler è un attore: protrebbe avere riterdi nella registrazione
		//Qak22Context.registerAsEventObserver(name,SystemData.fireEventId);
		//Qak22Context.registerAsEventObserver(name,SystemData.endAlarmId);
 	}
	
	@State( name = "robotStart", initial=true)
	@Transition( state = "robotMoving" ,  msgId = SystemData.endMoveOkId )
	@Transition( state = "alarm" ,        msgId = SystemData.fireEventId )
	@Transition( state = "wallDetected" , msgId = SystemData.endMoveKoId )
	protected void robotStart( IApplMessage msg ) {
		outInfo(""+msg + " connecting (blocking all the actors ) ... ");	
 		conn = WsConnection.create("localhost:8091" ); 	 
 		outInfo("connected "+conn);	
   		((WsConnection)conn).addObserver( new WsConnWEnvObserver(getName()) );
  		VRobotMoves.step(getName(),conn);
	}
	
 	@State( name = "robotMoving" )
	@Transition( state = "alarm" ,        msgId = SystemData.fireEventId )
 	@Transition( state = "robotMoving" ,  msgId = SystemData.endMoveOkId)
  	@Transition( state = "wallDetected" , msgId = SystemData.endMoveKoId )
	protected void robotMoving( IApplMessage msg ) {
		//outInfo(""+msg);	
		VRobotMoves.step(getName(),conn);
 	}
 	
 	@State( name = "alarm" )
 	@Transition( state = "endalarm" , msgId = SystemData.endAlarmId )
	protected void alarm( IApplMessage msg ) {
 		outInfo(""+msg);
 	}
 	@State( name = "endalarm" )
  	@Transition( state = "robotMoving" ,  msgId = SystemData.endMoveOkId )
	@Transition( state = "wallDetected" , msgId = SystemData.endMoveKoId )
 	protected void endalarm( IApplMessage msg ) {
 		outInfo(""+msg);
 	}
 	/*
 	 * Transizioni condizionate (con guardie)
 	 */
 	@State( name = "wallDetected" )
 	protected void wallDetected( IApplMessage msg ) {
		outInfo("ncorner="+ ncorner + " " + msg);	
		ncorner++;
		//Parte aggiunta al termine, per definire le transizioni
 		if( ncorner == 4 ) {
 			addTransition("endWork", null); //empty move
  		}else {
  			VRobotMoves.turnLeft(getName(), conn);
  			addTransition("robotMoving",  SystemData.endMoveOkId);
  		}
 	}
 	
 	@State( name = "endWork" )
 	protected void endWork( IApplMessage msg ) {
 		VRobotMoves.turnLeft(getName(), conn);
		outInfo("BYE" );	
 		System.exit(0);
 	}
 	

}

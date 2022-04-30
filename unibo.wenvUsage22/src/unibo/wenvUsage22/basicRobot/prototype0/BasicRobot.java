package unibo.wenvUsage22.cleaner.prototype0;

/*
 * BasicRobot riceve comandi aril da NaiveGui
 * Comando aril: 
 */

import it.unibo.kactor.IApplMessage;
import unibo.actor22.QakActor22FsmAnnot;
import unibo.actor22.annotations.State;
import unibo.actor22.annotations.Transition;
import unibo.actor22comm.interfaces.IObserver;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;
import unibo.actor22comm.ws.WsConnection;
import unibo.wenvUsage22.cleaner.fsm.WsConnApplObserver;
import unibo.wenvUsage22.common.VRobotMoves;
import unibo.wenvUsage22.common.ApplData;

public class BasicRobot extends QakActor22FsmAnnot{
	
	
	private Interaction2021 conn;
	private String robotCmdId = "move";
	private String robotName  = "vr";
	
 	
	public BasicRobot(String name) {
		super(name);
	}

	private  IApplMessage moveAril( String cmd  ) {
		switch( cmd ) {
			case "w" : return CommUtils.buildDispatch(getName(), robotCmdId, "moveForward(300)", robotName);
			case "s" : return CommUtils.buildDispatch(getName(), robotCmdId, "moveBAckward(300)",robotName);		
			case "a" : return CommUtils.buildDispatch(getName(), robotCmdId, "turnLeft(300)",    robotName);
			case "d" : return CommUtils.buildDispatch(getName(), robotCmdId, "turnRight(300)",   robotName);
			case "h" : return CommUtils.buildDispatch(getName(), robotCmdId, "alarm(300)",       robotName);
			default: return CommUtils.buildDispatch(getName(),   robotCmdId, "alarm(300)",       robotName);
		}		 
	}
	private  String move( String cmd  ) {
		switch( cmd ) {
			case "w" : return  ApplData.moveForward(300) ;
			case "s" : return  ApplData.moveBackward(300);		
			case "a" : return  ApplData.turnLeft(300);
			case "d" : return  ApplData.turnRight(300);
			case "h" : return  ApplData.stop(100);
			default: return    ApplData.stop(100);
		}		 
	}

	private void robotMove( String cmd ) {
		//Dovrebbe un supporto che trasla il comando cmd in cril 
		try {
//			IApplMessage cmdMsg = move( cmd );
//			ColorsOut.outappl(getName() + " | robotMove cmdMsg:" + cmdMsg,  ColorsOut.BLUE);
			conn.forward( move(cmd) );
		}catch( Exception e) {
			ColorsOut.outerr( getName() +  " | robotMove ERROR:" +  e.getMessage() );
		}			
	}
	
	protected void init() {
		//Dovrebbe distingure tra diversi tipi di robot e usare un supporto che realizza
 		ColorsOut.outappl(getName() + " | ws connecting ...." ,  ColorsOut.BLUE);
		conn = WsConnection.create("localhost:8091" ); 
		IObserver robotMoveObserver = new WsConnApplObserver(getName());
		((WsConnection)conn).addObserver(robotMoveObserver);
 		ColorsOut.outappl(getName() + " | conn:" + conn,  ColorsOut.BLUE);
	}
	
	@State( name = "start", initial=true)
	@Transition( state = "work"    )  //empty move
 	protected void start( IApplMessage msg ) {
		outInfo(""+msg);
		init();
	}
	
	@State( name = "work" )
 	@Transition( state = "work",     msgId="cmd"  ) 
//	@Transition( state = "turnGoingDown", msgId="endMoveKo"  ) 
	protected void work( IApplMessage msg ) {
		outInfo(""+msg);
		String cmd = msg.msgContent();
		outInfo("cmd="+cmd);
		//robotMove(  "a" ); 

	//if( msg.msgId().equals("robotCmd") ) forward( VRobotMoves.move(msg.msgContent(), getName(), getName()));
 	}
	
 	
	
	@State( name = "endJob" )
	protected void endJob( IApplMessage msg ) {
		outInfo(""+msg);
   	}
	
 
	
}

 
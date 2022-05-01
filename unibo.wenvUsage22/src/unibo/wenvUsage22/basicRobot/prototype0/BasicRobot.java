package unibo.wenvUsage22.basicRobot.prototype0;

/*
 * BasicRobot riceve comandi aril da NaiveGui
 * Comando aril: 
 */

import it.unibo.kactor.IApplMessage;
 
import unibo.actor22.QakActor22FsmAnnot;
import unibo.actor22.annotations.State;
import unibo.actor22.annotations.Transition;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.interfaces.IObserver;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;
import unibo.actor22comm.ws.WsConnection;
import unibo.wenvUsage22.cleaner.fsm.WsConnApplObserver;
import unibo.wenvUsage22.common.ApplData;
import unibo.wenvUsage22.common.VRobotMoves;

 


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
	private  String arilToCril( String cmd  ) {
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
		try {
			conn.forward( arilToCril(cmd) );
		}catch( Exception e) {
			ColorsOut.outerr( getName() +  " | robotMove ERROR:" +  e.getMessage() );
		}			
	}
	
	protected void init() {
		//Dovrebbe distingure tra diversi tipi di robot e usare un supporto che realizza
 		ColorsOut.outappl(getName() + " | ws connecting ...." ,  ColorsOut.YELLOW);
		conn = WsConnection.create("localhost:8091" ); 
		IObserver robotMoveObserver = new WsConnApplObserver(getName()); //genera endMoveOk / endMoveKo
		((WsConnection)conn).addObserver(robotMoveObserver);
 		ColorsOut.outappl(getName() + " | conn:" + conn,  ColorsOut.YELLOW);
	}

	@State( name = "activate", initial=true)
	@Transition( state = "start",   msgId= SystemData.startSysCmdId  )
	protected void activate( IApplMessage msg ) {
		outInfo(""+msg);
		init();
 	}

	@State( name = "start" )
	@Transition( state = "work", msgId="move"  )
	protected void start( IApplMessage msg ) {
		outInfo(""+msg); //activate ...
	}


	@State( name = "work" )
 	//@Transition( state = "work", msgId="move"  )
	//@Transition( state = "waitMoveResult" )
	@Transition( state = "handleOk", msgId="endMoveOk"  )
	@Transition( state = "handleKo", msgId="endMoveKo"  )
	protected void work( IApplMessage msg ) {
		outInfo(""+msg);  //msg(move,dispatch,,basicrobot,w,3)
		String cmd = msg.msgContent().replace("'","");
		//VRobotMoves.step(getName(), conn );
		//VRobotMoves.moveForward( getName(),conn,300 );
		robotMove(cmd);
 	}

	@State( name = "handleOk" )
	@Transition( state = "work", msgId="move"  )
 	protected void handleOk( IApplMessage msg ) {
		outInfo(""+msg);
		this.updateResourceRep22("testttttttttttttttttttt");
//		try {
//			WebSocketConfiguration.wshandler.sendToAll( new TextMessage( msg.toString() ) );
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
 	}
	@State( name = "handleKo" )
	@Transition( state = "work", msgId="move"  )
	protected void handleKo( IApplMessage msg ) {
		outInfo(""+msg);
//		try {
//			WebSocketConfiguration.wshandler.sendToAll( new TextMessage( msg.toString() ) );
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	
	@State( name = "endJob" )
	protected void endJob( IApplMessage msg ) {
		outInfo(""+msg);
   	}
	
 
	
}

 
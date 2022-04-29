package unibo.wenvUsage22.cleaner.prototype0;

/*
 * 
 */

import it.unibo.kactor.IApplMessage;
import unibo.actor22.QakActor22FsmAnnot;
import unibo.actor22.annotations.State;
import unibo.actor22.annotations.Transition;
import unibo.actor22comm.interfaces.IObserver;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.ws.WsConnection;
import unibo.wenvUsage22.cleaner.fsm.WsConnApplObserver;
import unibo.wenvUsage22.common.VRobotMoves;
import unibo.actor22.QakActor22FsmAnnot;

public class BasicRobot extends QakActor22FsmAnnot{
	private Interaction2021 conn;
 
	private int numIter     = 0;
	private int numIterOk   = 5;
	private int turnStep    = 800;   //600 => too fast
 	
	public BasicRobot(String name) {
		super(name);
	}

	protected void init() {
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
     	//VRobotMoves.step(getName(), conn );  
	}
	
	@State( name = "work" )
//	@Transition( state = "goingDown",     msgId="endMoveOk"  ) 
//	@Transition( state = "turnGoingDown", msgId="endMoveKo"  ) 
	protected void work( IApplMessage msg ) {
		outInfo(""+msg);
 	}
	
 	
	
	@State( name = "endJob" )
	protected void endJob( IApplMessage msg ) {
		outInfo(""+msg);
		outInfo("numIter="+numIter);
		if( numIter == numIterOk ) ColorsOut.outappl(getName() + " | DONE " ,  ColorsOut.MAGENTA);  
		else ColorsOut.outerr(getName() + " | TOO FAST "  );  
  	}
	
 
	
}


//@State( name = "checkEndmove" )
//protected void checkEndmove( IApplMessage msg ) {
//	outInfo(""+msg);
//	String mc         = msg.msgContent().replace("'","");
//	JSONObject mcJson = new JSONObject(mc);
//	ColorsOut.outappl(getName() + " | mcJson:" + mcJson,  ColorsOut.GREEN);
//	boolean result =  mcJson.has("endmove") && mcJson.getBoolean("endmove");
//	if ( result) {
//        ColorsOut.out(" endStepDown " );			
//		addTransition( "endStepDown", ApplData.resumeCmd );
//		}else {
//			ColorsOut.out(" endJob " );			
//			addTransition( "endJob", ApplData.resumeCmd );
//		}
//	this.sendMsgToMyself(ApplData.resumeCmd(getName(),getName()) );
//}

package unibo.wenvUsage22.cleaner.fsm;

/*
 * WsConnection è associata a un observer che emette SystemData.wsEventId
 */
import org.json.JSONObject;
import it.unibo.kactor.IApplMessage;
import unibo.actor22.QakActor22;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.ws.WsConnectionForActors;
import unibo.wenvUsage22.actors.QakActor22Fsm;
import unibo.wenvUsage22.actors.QakActor22FsmAnnot;
import unibo.wenvUsage22.annot.State;
import unibo.wenvUsage22.annot.Transition;
import unibo.wenvUsage22.annot.TransitionGuarded;
import unibo.wenvUsage22.common.ApplData;
import unibo.wenvUsage22.cleaner.VRobotMoves;


public class ActorRobotCleanerFsm extends QakActor22FsmAnnot{
	private Interaction2021 conn;
 
	private int numIter     = 0;
	
	public ActorRobotCleanerFsm(String name) {
		super(name);
	}

	protected void init() {
		conn = WsConnectionForActors.create("localhost:8091", getName() ); //con owner
 		ColorsOut.outappl(getName() + " | conn:" + conn,  ColorsOut.BLUE);
	}
	
	@State( name = "start", initial=true)
	@Transition( state = "endStepDown", msgId="wsEvent" )
	protected void start( IApplMessage msg ) {
		outInfo(""+msg);
		init();
    	VRobotMoves.step(getName(), conn );  
	}
	
	@State( name = "endStepDown" )
 	@Transition( state = "checkEndmove", msgId="wsEvent"  )  //GUARDIA ???
	protected void endStepDown( IApplMessage msg ) {
		outInfo(""+msg);
		VRobotMoves.step(getName(), conn );
	}
	
	@State( name = "checkEndmove" )
	protected void checkEndmove( IApplMessage msg ) {
		outInfo(""+msg);
		String mc         = msg.msgContent().replace("'","");
		JSONObject mcJson = new JSONObject(mc);
		ColorsOut.outappl(getName() + " | mcJson:" + mcJson,  ColorsOut.GREEN);
		boolean result =  mcJson.has("endmove") && mcJson.getBoolean("endmove");
		if ( result) {
	        ColorsOut.out(" endStepDown " );			
			addTransition( "endStepDown", ApplData.resumeCmd );
 		}else {
 			ColorsOut.out(" endJob " );			
 			addTransition( "endJob", ApplData.resumeCmd );
 		}
		this.sendMsgToMyself(ApplData.resumeCmd(getName(),getName()) );
	}

	
	@State( name = "endJob" )
	protected void endJob( IApplMessage msg ) {
		outInfo(""+msg);
	}
	



	@Override
	public void handleAsObserver(String data) {
		outInfo(""+data);		
	}
	
}

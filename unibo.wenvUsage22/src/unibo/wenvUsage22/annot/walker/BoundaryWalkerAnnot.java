package unibo.wenvUsage22.annot.walker;

import java.util.Observable;

import org.json.JSONObject;

import it.unibo.kactor.IApplMessage;
import unibo.actor22comm.http.HttpConnection;
import unibo.actor22comm.interfaces.IObserver;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;
import unibo.actor22comm.ws.WsConnection;
import unibo.wenvUsage22.actors.QakActor22FsmAnnot;
import unibo.wenvUsage22.actors.SysData;
import unibo.wenvUsage22.annot.State;
import unibo.wenvUsage22.annot.Transition;
import unibo.wenvUsage22.common.ApplData;

public class BoundaryWalkerAnnot extends QakActor22FsmAnnot  {
	private Interaction2021 conn;
	
//	private boolean wallDetected = false;
	private String  wallName     = "unknown";
	private int ncorner          = 0;
	
	public BoundaryWalkerAnnot(String name) {
		super(name);
 	}
	
	protected void doStep() {
		sendMsgToMyself( ApplData.w( getName() , ApplData.robotName ) );
	}
	protected void turnLeft() {
		sendMsgToMyself( ApplData.a( getName() , ApplData.robotName ) );
	}
	protected void halt() {
		sendMsgToMyself( SysData.haltSysCmd(getName(),getName()) );
	}
	
	@State( name = "robotStart", initial=true)
	@Transition( state = {"robotMoving" }, 
	             msgId = { ApplData.robotCmdId })
	protected void robotStart( IApplMessage msg ) {
		outInfo(""+msg + " connecting (blocking all the actors ) ... ");	
		//Inizializzo la connessione con WEnv
 		conn = new WsConnection("localhost:8091");//WsConnection.create("localhost:8091" );				 
 		outInfo("connected "+conn);	
		//Aggiungo l'attore come observer dei messaggi inviati da WEnv (vedi update)
		((WsConnection)conn).addObserver((IObserver) myself);
		//sendMsgToMyself( ApplData.w( getName() , ApplData.robotName ) );
		doStep();
	}
	
 	@State( name = "robotMoving" )
	@Transition( state = {"robotMoving",        "wallDetected" }, 
	             msgId = { ApplData.robotCmdId, ApplData.wallDetectedId })
	protected void robotMoving( IApplMessage msg ) {
		outInfo(""+msg);	
//		if( wallDetected ) sendMsgToMyself( ApplData.wallDetected(getName(),wallName ));
//		else 
			doMove( msg.msgContent().replaceAll("'","") ); //remove ' ' 		
	}
 	
 	@State( name = "wallDetected" )
	@Transition( state = {"robotMoving",       "endWork" }, 
	             msgId = { ApplData.robotCmdId, SysData.haltSysCmdId })
	protected void wallDetected( IApplMessage msg ) {
		outInfo(""+msg);	
//		wallDetected = false;
		wallName     = "unknown";
		ncorner++;
		if( ncorner == 4 ) //sendMsgToMyself( SysData.haltSysCmd(getName(),getName()) );
			halt();
		else //sendMsgToMyself( ApplData.a( getName() , ApplData.robotName ) );
			turnLeft();
 	}
 	
 	@State( name = "endWork" )
 	protected void endWork( IApplMessage msg ) {
		outInfo(""+msg);	
 		
 	}
 	
	
	protected void doMove(String move) {
		try {
			outInfo("doMove:"+move);	
			conn.forward( move );
		}catch( Exception e) {
			ColorsOut.outerr("robotMOve ERROR:" + e.getMessage() );
		}
	}


	@Override
	public void handleAsObserver(String data) {
		ColorsOut.out(getName() + " |  asObserver receives:" + data);
 		JSONObject d = new JSONObject(""+data);
 		if( d.has("collision") ) {
// 			wallDetected = true;
 			wallName     = d.getString("target");
 			sendMsgToMyself( ApplData.wallDetected(getName(),wallName ));
 		}else if( d.has("endmove") ) {
 			if( d.getBoolean("endmove")) {
 				CommUtils.delay(300);  //to reduce speed
 				sendMsgToMyself( ApplData.w( getName() , ApplData.robotName ) );
 			}else ColorsOut.outerr(getName() + " |  inconsistent "  );
 		}
	}

}

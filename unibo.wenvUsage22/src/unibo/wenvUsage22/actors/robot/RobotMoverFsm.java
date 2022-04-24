package unibo.wenvUsage22.actors.robot;
import org.json.JSONObject;
import it.unibo.kactor.IApplMessage;
import unibo.actor22comm.interfaces.IObserver;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.interfaces.StateActionFun;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;
import unibo.actor22comm.ws.WsConnection;
import unibo.wenvUsage22.actors.QakActor22Fsm;
import unibo.wenvUsage22.common.ApplData;


public  class RobotMoverFsm extends QakActor22Fsm { //implements IObserver
	private Interaction2021 conn;
//	private static IObserver myself;
	public RobotMoverFsm(String name) {
		super(name);
//		myself = this;
 	}
	
	protected void s0(IApplMessage msg) {
		outInfo(""+msg);	
		//Inizializzo la connessione con WEnv
		conn = WsConnection.create("localhost:8091" );				 
		//Aggiungo l'attore come observer dei messaggi inviati da WEnv (vedi update)
//		((WsConnection)conn).addObserver(myself);
	}	
  	 
	@Override
	protected void declareTheStates( ) {
		declareState("s0", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
//				outInfo(""+msg);	
//				//Inizializzo la connessione con WEnv
//				conn = WsConnection.create("localhost:8091" );				 
//				//Aggiungo l'attore come observer dei messaggi inviati da WEnv (vedi update)
//				((WsConnection)conn).addObserver(myself);
				s0( msg );
				//Muovo il robot in funzione del comando che mi arriva
				addTransition( "moveTheRobot", ApplData.robotCmdId );
				nextState();
			}			
		});
		declareState("moveTheRobot", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
				outInfo(""+msg);	
				doMove( msg.msgContent().replaceAll("'","") ); //remove ' ' 
				addTransition( "moveTheRobot",  ApplData.robotCmdId );
				addTransition( "s3", ApplData.haltSysCmdId );
				nextState();
			}			
		});
		declareState("s3", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
				outInfo(""+msg);
				outInfo("BYE" );
				addTransition( "s3", ApplData.haltSysCmdId );
  			}			
		});
	}
	
	@Override
	protected void setTheInitialState( ) {
		declareAsInitialState( "s0" );
	}
	
	protected void doMove(String move) {
		try {
 			conn.forward( move );
		}catch( Exception e) {
			ColorsOut.outerr("robotMOve ERROR:" + e.getMessage() );
		}
	}

 
/*	
	@Override
	public void handleAsObserver(String data) {
//		ColorsOut.out(getName() + " |  update receives:" + data);
		ColorsOut.outappl( getName() + " | asobserver receives:" + data, ColorsOut.MAGENTA);
 		JSONObject d = new JSONObject(""+data);
 		if( d.has("endmove") ) {
 			boolean result = d.getBoolean("endmove");
 			if( result ) {
	 			CommUtils.delay(300);
	 			//Qak22Util.sendAMsg( ApplData.w("main",getName() ) );
	 			//forward( ApplData.w("main",getName() ));
	 			autoMsg(ApplData.w("main",getName() ));
 			}
 			return;
 		}
        //Otherwise ...
 			autoMsg( ApplData.haltSysCmd("main",getName()) );	
	}	
*/

}

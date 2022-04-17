package unibo.wenvUsage22.actors;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Vector;
import it.unibo.kactor.IApplMessage;
import kotlin.Pair;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.QakActor22;
import unibo.actor22comm.http.HttpConnection;
import unibo.actor22comm.interfaces.IObserver;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.interfaces.StateActionFun;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
import unibo.actor22comm.ws.WsConnection;
import unibo.wenvUsage22.common.ApplData;


public  class RobotMoverFsm extends QakActor22Fsm implements IObserver{
 	private IApplMessage startCmd, moveCmd, haltCmd;
	private Interaction2021 conn;
	
	public RobotMoverFsm(String name) {
		super(name);
 		conn = WsConnection.create("localhost:8091" );
		((WsConnection)conn).addObserver(this);
		startCmd = CommUtils.buildDispatch("main", "activate", "activate",name );
		moveCmd  = CommUtils.buildDispatch("main", "move", "100",name );
		haltCmd  = CommUtils.buildDispatch("main", "halt", "100",name );
 	}
  	 
	protected void initStateMap( ) {
		stateMap.put("s0", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
				outInfo(""+msg);	
				addTransition( "s1", moveCmd.msgId() );
				nextState();
			}			
		});
		stateMap.put("s1", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
				outInfo(""+msg);	
				addTransition( "s1", moveCmd.msgId() );
				addTransition( "s3", haltCmd.msgId() );
				nextState();
			}			
		});
		stateMap.put("s3", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
				outInfo(""+msg);
				outInfo("BYE" );
				addTransition( "s3", haltCmd.msgId() );
  			}			
		});
		curState = "s0";
		addExpecetdMsg(curState, startCmd.msgId());
	}
	
	protected void xxx() throws Exception {
  		conn = HttpConnection.create("localhost:8090" ); //INTERROMPIBILE usando WebGui
  		String answer = "";
  		boolean obstacle = false;
  		//Trasformo in FSM che gestisce messaggi inviati da WS
  		for( int i=1; i<=4; i++ ) {
	  		while( ! obstacle ) {
	  			answer = conn.request( ApplData.moveForward(500) ); 
	  			ColorsOut.outappl(i + " answer= " + answer, ColorsOut.BLACK  );
	  			obstacle = answer.contains("collision");
	  			CommUtils.delay(500);
	  		}
	  		obstacle = false;
	  		answer = conn.request( ApplData.turnLeft(300) );
  		}
 
		ColorsOut.outappl("answer= " + answer, ColorsOut.BLACK  );		
	}
	
	public void doJob() throws Exception {
		CommSystemConfig.tracing = false;
		initStateMap( ) ;
		
 			conn.forward( ApplData.moveForward(500) );
 
 		
		//new EnablerContextForActors( "ctx",8030,ProtocolType.tcp).activate();
//		Qak22Context.showActorNames();
//		CommUtils.delay(1000);
//		//Qak22Util.sendAMsg( startCmd );
//		Qak22Util.sendAMsg( moveCmd );
//		//Qak22Util.sendAMsg( haltCmd );
//		Qak22Util.sendAMsg( moveCmd );
//		Qak22Util.sendAMsg( haltCmd );
//		Qak22Util.sendAMsg( startCmd );
//		CommUtils.delay(2000);
	}
	


	@Override
	public void update(Observable source, Object data) {
		ColorsOut.out( getName() + " | update/2 receives:" + data);
//		JSONObject d = new JSONObject(""+data);
//		ColorsOut.outappl("ClientUsingWs update/2 collision=" + d.has("collision"), ColorsOut.MAGENTA);
		
	}
	@Override
	public void update(String data) {
		ColorsOut.out(getName() + " |  update receives:" + data);
	}	

	
//---------------------------------------------------------
	public static void main( String[] args) throws Exception {
		CommUtils.aboutThreads("Before start - ");
		new RobotMoverFsm("a1").doJob();
		CommUtils.aboutThreads("Before end - ");		
	}
}

package unibo.wenvUsage22.actors;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import it.unibo.kactor.IApplMessage;
import kotlin.Pair;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.QakActor22;
import unibo.actor22comm.interfaces.StateActionFun;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;


public  class Actor22Fsm extends QakActor22{
	private  HashMap<String,StateActionFun> stateMap = new HashMap<String,StateActionFun>();
	private  HashMap<String,String> nextMsgMap       = new HashMap<String,String>();
	private  Vector<IApplMessage>  OldMsgQueue       = new Vector< IApplMessage>();
	private  String curState = "";
	private IApplMessage startCmd, moveCmd, haltCmd;
 	
	public Actor22Fsm(String name) {
		super(name);
		startCmd = CommUtils.buildDispatch("main", "activate", "activate",name );
		moveCmd  = CommUtils.buildDispatch("main", "move", "100",name );
		haltCmd  = CommUtils.buildDispatch("main", "halt", "100",name );
		initStateMap( );
	}

	//protected abstract void initStateMap();
	
	private Vector< Pair<String, String> > t = new Vector< Pair<String, String> >();
	
	protected void addInterest(String state, String msgId) {
		ColorsOut.outappl( getName() + " in " + curState + " | transition to " + state + " for " +  msgId, ColorsOut.BLUE);		
		t.add( new Pair<>(state, msgId) );
	}
	 
	protected void initStateMap( ) {
		stateMap.put("s0", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
				outInfo(""+msg);	
				addInterest( "s1", moveCmd.msgId() );
				//t.add( new Pair<>("s1", moveCmd.msgId()) );
				nextState();
 				//Pair<String, String> pair = new Pair<>(1, "One");
				//transition( "s1", moveCmd.msgId() );
			}			
		});
		stateMap.put("s1", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
				outInfo(""+msg);	
				addInterest( "s1", moveCmd.msgId() );
				addInterest( "s3", haltCmd.msgId() );
//				t.add( new Pair<>("s1", moveCmd.msgId()) );
//				t.add( new Pair<>("s3", haltCmd.msgId()) );
				nextState();
//				transition( "s1", moveCmd.msgId() );
//				transition( "s3", haltCmd.msgId() );
			}			
		});
		stateMap.put("s3", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
				outInfo(""+msg);
				outInfo("BYE" );
				addInterest( "s3", haltCmd.msgId() );
  			}			
		});
		curState = "s0";
		//transition( "s0", startCmd.msgId() );
		nextMsgMap.put(startCmd.msgId(), curState);
		ColorsOut.outappl("DONE initStateMap for " + this.getName() , ColorsOut.BLACK);
	}
	
	protected void outInfo(String info) {
		ColorsOut.outappl(curState + " | " + info, ColorsOut.BLACK);
	}
	
//	protected void transition(String state, String msgId) {
//		//clearInterest( );
//		ColorsOut.outappl( getName() + " in " + curState + " | transition to " + state + " for " +  msgId, ColorsOut.BLUE);
//		//Cerco in coda temporanea
//		IApplMessage oldMsg = searchInOldMsgQueue( msgId );
//		if( oldMsg != null ) stateTransition(state,oldMsg);
//		else  waitForMessage(state,msgId);
//	}
	
	protected void nextState() {
		nextMsgMap.clear();
		Iterator< Pair<String, String> > iter = t.iterator();
		while( iter.hasNext() ) {
			Pair<String, String> v = iter.next();
			String state = v.getFirst();
			String msgId = v.getSecond();
			IApplMessage oldMsg = searchInOldMsgQueue( msgId );
			if( oldMsg != null ) {
				stateTransition(state,oldMsg);
				break;
			}
			else  nextMsgMap.put(msgId, state);	
		}
	}
	
//	protected void waitForMessage(String state, String msgId) {
//		//msgMap.clear();
//		nextMsgMap.put(msgId, state);		
//	}
	
	protected void stateTransition(String stateName, IApplMessage msg ) {
		curState = stateName;
		t.removeAllElements();
		StateActionFun a = stateMap.get(stateName);
		a.run( msg );			
	}
 
	protected void clearInterest( ) {
		nextMsgMap.clear();
	}
	
	@Override
	protected void handleMsg(IApplMessage msg) {
		ColorsOut.outappl(getName() + " | handleMsg " +  msg, ColorsOut.GREEN);
		String state = checkInterest(msg);
		if ( state != null ) { stateTransition(state,msg);
//			StateActionFun a = stateMap.get(state);
//			a.run( msg );
		}else { memoTheMessage(msg);
//			ColorsOut.outappl(getName() + " | handleMsg not yet:" +  msg, ColorsOut.YELLOW);
//			OldMsgQueue.add(msg);
		}
	}
	
	protected void memoTheMessage(IApplMessage msg) {
		ColorsOut.outappl(getName() + " | handleMsg not yet:" +  msg, ColorsOut.YELLOW);
		OldMsgQueue.add(msg);		
	}

	protected IApplMessage searchInOldMsgQueue(String msgId) {
 		Iterator<IApplMessage> iter = OldMsgQueue.iterator();
		while( iter.hasNext() ) {
			IApplMessage msg = iter.next();
			if( msg.msgId().equals(msgId)) {
				OldMsgQueue.remove(msg);
				return msg;
			}			
		}
		return null;
 	}
	protected String checkInterest(IApplMessage msg) {
		return nextMsgMap.get( msg.msgId() );
	}
	
	
	
	public void doJob() {
		CommSystemConfig.tracing = false;
		//new EnablerContextForActors( "ctx",8030,ProtocolType.tcp).activate();
		Qak22Context.showActorNames();
		CommUtils.delay(1000);
		//Qak22Util.sendAMsg( startCmd );
		Qak22Util.sendAMsg( moveCmd );
		//Qak22Util.sendAMsg( haltCmd );
		Qak22Util.sendAMsg( moveCmd );
		Qak22Util.sendAMsg( haltCmd );
		Qak22Util.sendAMsg( startCmd );
		CommUtils.delay(2000);
	}
	
	public static void main( String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new Actor22Fsm("a1").doJob();
		CommUtils.aboutThreads("Before end - ");
		
	}
	
}

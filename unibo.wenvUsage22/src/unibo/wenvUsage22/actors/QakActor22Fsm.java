package unibo.wenvUsage22.actors;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Vector;
import it.unibo.kactor.IApplMessage;
import kotlin.Pair;
import unibo.actor22.QakActor22;
import unibo.actor22comm.interfaces.IObserver;
import unibo.actor22comm.interfaces.StateActionFun;
import unibo.actor22comm.utils.ColorsOut;
 


public abstract class QakActor22Fsm extends QakActor22 implements IObserver{
	private HashMap<String,StateActionFun> stateMap = new HashMap<String,StateActionFun>();
	protected HashMap<String,String> nextMsgMap       = new HashMap<String,String>();
	protected Vector<IApplMessage>  OldMsgQueue       = new Vector< IApplMessage>();
	protected Vector< Pair<String, String> > transTab = new Vector< Pair<String, String> >();
	private String curState = "";
  	
	public QakActor22Fsm(String name) {
		super(name);
		declareTheStates( );
		setTheInitialState( );
		addExpecetdMsg(curState, SysData.startSysCmdId );
		ColorsOut.outappl(getName() + " | autoMsg SysData.startSysCmd", ColorsOut.GREEN);
		autoMsg(SysData.startSysCmd("system",name));
	}
 	
 	
	protected abstract void declareTheStates( );
	protected abstract void setTheInitialState( );
	
	protected void declareAsInitialState( String stateName ) {
		ColorsOut.outappl( getName() + " QakActor22Fsm | declareAsInitialState " + stateName, ColorsOut.BLUE);		
		curState = stateName;
	};
	
	protected void declareState(String stateName, StateActionFun action) {
		ColorsOut.outappl( getName() + " QakActor22Fsm | declareState " + stateName, ColorsOut.BLUE);		
		stateMap.put( stateName, action );
	}
	
	protected void addTransition(String state, String msgId) {
		//ColorsOut.outappl( getName() + " QakActor22Fsm | in " + curState + " | transition to " + state + " for " +  msgId, ColorsOut.BLUE);		
		transTab.add( new Pair<>(state, msgId) );
	}
	
	
	protected void nextState() {
		clearExpectedMsgs();
		Iterator< Pair<String, String> > iter = transTab.iterator();
		while( iter.hasNext() ) {
			Pair<String, String> v = iter.next();
			String state = v.getFirst();
			String msgId = v.getSecond();
			IApplMessage oldMsg = searchInOldMsgQueue( msgId );
			if( oldMsg != null ) {
				stateTransition(state,oldMsg);
				break;
			}
			else  addExpecetdMsg(state, msgId);
		}
	}
	protected void stateTransition(String stateName, IApplMessage msg ) {
		curState = stateName;
		transTab.removeAllElements();
		StateActionFun a = stateMap.get(stateName);
		if( a != null ) a.run( msg );
		else ColorsOut.outerr(getName() + " | QakActor22Fsm TERMINATED");
	}	
	protected void addExpecetdMsg(String state, String msgId) {
		nextMsgMap.put(msgId, state);		
	}
	protected void clearExpectedMsgs( ) {
		nextMsgMap.clear();		
	}	
	protected String checkIfExpected(IApplMessage msg) {
		return nextMsgMap.get( msg.msgId() );
	}
	
 	
	@Override
	protected void handleMsg(IApplMessage msg) {
		//ColorsOut.out(getName() + " | QakActor22Fsm handleMsg " +  msg, ColorsOut.GREEN);
		String state = checkIfExpected(msg);
		if ( state != null ) stateTransition(state,msg);
		else memoTheMessage(msg);
	}
	
	protected void memoTheMessage(IApplMessage msg) {
		ColorsOut.outappl(getName() + " | QakActor22Fsm handleMsg not yet:" +  msg, ColorsOut.YELLOW);
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

	protected void outInfo(String info) {
		ColorsOut.outappl(getName() + "/" + curState + " | " + info, ColorsOut.BLACK);
	}
	
//Useful for IObserver	
	
	@Override
	public void update(Observable arg0, Object arg1) {
		handleAsObserver(arg1.toString());
	}

//Convert ino an event ???? Or abstract ???	
	@Override
	public void update(String data) {
		ColorsOut.outerr(curState + " | update NEVER HERE " );
	} 
 	
	public abstract void handleAsObserver(String data) ;
 	
}

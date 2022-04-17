package unibo.wenvUsage22.actors;
import it.unibo.kactor.IApplMessage;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22comm.interfaces.StateActionFun;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;


public  class FirstQakActor22Fsm extends QakActor22Fsm{
 	private IApplMessage startCmd, moveCmd, haltCmd;
 	
	public FirstQakActor22Fsm(String name) {
		super(name);
		startCmd = CommUtils.buildDispatch("main", "activate", "activate",name );
		moveCmd  = CommUtils.buildDispatch("main", "move", "100",name );
		haltCmd  = CommUtils.buildDispatch("main", "halt", "100",name );
		initStateMap( );
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
	
 	
}

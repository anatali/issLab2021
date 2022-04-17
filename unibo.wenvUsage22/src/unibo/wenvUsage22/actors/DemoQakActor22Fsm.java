package unibo.wenvUsage22.actors;
import it.unibo.kactor.IApplMessage;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22comm.interfaces.StateActionFun;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;


public  class DemoQakActor22Fsm extends QakActor22Fsm{
 	private IApplMessage startCmd, moveCmd, haltCmd;
 	
	public DemoQakActor22Fsm(String name) {
		super(name);
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
	
	public void doJob() {
		CommSystemConfig.tracing = false;
 		initStateMap( );

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
		new DemoQakActor22Fsm("a1").doJob();
		CommUtils.aboutThreads("Before end - ");
		
	}
	
}

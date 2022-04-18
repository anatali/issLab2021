package unibo.wenvUsage22.actors;
import it.unibo.kactor.IApplMessage;
import unibo.actor22comm.interfaces.StateActionFun;
import unibo.wenvUsage22.common.ApplData;


public  class FirstQakActor22Fsm extends QakActor22Fsm{
 	
	public FirstQakActor22Fsm(String name) {
		super(name);
 	}
	
	/*
    @State @Initial
    @WhenRequest("t0", "replyToRequest", "whoareyou")
    suspend fun s0() {
        actorPrintln("waiting for next request...")
    }
    */
  	
	@Override
	protected void declareTheStates( ) {
		declareState( "s0", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
				outInfo(""+msg);	
				addTransition( "s1", ApplData.moveCmdId );
				nextState();
			}			
		});
		declareState("s1", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
				outInfo(""+msg);	
				addTransition( "s1", ApplData.moveCmdId );
				addTransition( "s3", SysData.haltSysCmdId );
				nextState();
			}			
		});
		declareState("s3", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
				outInfo(""+msg);
				outInfo("BYE" );
				addTransition( "s3", SysData.haltSysCmdId );
  			}			
		});
	}
	
	@Override
	protected void setTheInitialState( ) {
		declareAsInitialState( "s0" );
	}
}

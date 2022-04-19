package unibo.wenvUsage22.actors;
import it.unibo.kactor.IApplMessage;
import unibo.actor22comm.interfaces.StateActionFun;
import unibo.actor22comm.utils.ColorsOut;
import unibo.wenvUsage22.common.ApplData;


public  class FirstQakActor22Fsm extends QakActor22Fsm {
 	
	public FirstQakActor22Fsm(String name) {
		super(name);
 	}
	
   	
	protected void s0(IApplMessage msg) {
		//ColorsOut.outappl( getName()  + " | handleMsg " + msg, ColorsOut.CYAN);	
		outInfo("uuuuuuuuuuuuuuuuuuuuuuu "+msg);
	}

	@Override
	protected void declareTheStates( ) {
		declareState( "s0", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
				s0(msg);
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


	@Override
	public void handleAsObserver(String data) {
		// TODO Auto-generated method stub
		
	}
}

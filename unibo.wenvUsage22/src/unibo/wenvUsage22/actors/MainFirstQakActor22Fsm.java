package unibo.wenvUsage22.actors;
import it.unibo.kactor.IApplMessage;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22comm.interfaces.StateActionFun;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;


public  class MainFirstQakActor22Fsm  {
 	private IApplMessage startCmd, moveCmd, haltCmd;
 	
	public MainFirstQakActor22Fsm( String name) {
 		startCmd = CommUtils.buildDispatch("main", "activate", "activate",name );
		moveCmd  = CommUtils.buildDispatch("main", "move", "100",name );
		haltCmd  = CommUtils.buildDispatch("main", "halt", "100",name );
 		new FirstQakActor22Fsm( name ) ;
 	}
  	 
 	
	public void doJob() {
		CommSystemConfig.tracing = false;

		//new EnablerContextForActors( "ctx",8030,ProtocolType.tcp).activate();
		Qak22Context.showActorNames();
 		Qak22Util.sendAMsg( startCmd );
		Qak22Util.sendAMsg( moveCmd );
		//Qak22Util.sendAMsg( haltCmd );
		Qak22Util.sendAMsg( moveCmd );
		Qak22Util.sendAMsg( haltCmd );
		//Qak22Util.sendAMsg( startCmd );
		CommUtils.delay(2000);
	}
	
	public static void main( String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new MainFirstQakActor22Fsm( "a1" ).doJob();
		CommUtils.aboutThreads("Before end - ");
		
	}
	
}

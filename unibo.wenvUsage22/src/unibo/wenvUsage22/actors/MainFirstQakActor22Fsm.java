package unibo.wenvUsage22.actors;
import it.unibo.kactor.IApplMessage;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22comm.interfaces.StateActionFun;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
import unibo.wenvUsage22.common.ApplData;


public  class MainFirstQakActor22Fsm  {
// 	private IApplMessage startCmd, moveCmd, haltCmd;
 	
	public MainFirstQakActor22Fsm( ) {
// 		startCmd = CommUtils.buildDispatch("main", "activate", "activate",name );
//		moveCmd  = CommUtils.buildDispatch("main", "move", "100",name );
//		haltCmd  = CommUtils.buildDispatch("main", "halt", "100",name );
 	}
  	 
 	
	public void doJob( String name ) {
		CommSystemConfig.tracing = false;
 		new FirstQakActor22Fsm( name ) ;

		//new EnablerContextForActors( "ctx",8030,ProtocolType.tcp).activate();
		Qak22Context.showActorNames();
// 		Qak22Util.sendAMsg( startCmd );
		Qak22Util.sendAMsg( ApplData.moveCmd("main", name, "w" ) );
		//Qak22Util.sendAMsg( haltCmd );
		Qak22Util.sendAMsg( ApplData.moveCmd("main", name, "w" ) );
		Qak22Util.sendAMsg( SysData.haltSysCmd("main", name) );
		//Qak22Util.sendAMsg( startCmd );
		CommUtils.delay(2000);
	}
	
	public static void main( String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new MainFirstQakActor22Fsm(  ).doJob( "a1" );
		CommUtils.aboutThreads("Before end - ");
		
	}
	
}

package unibo.wenvUsage22.actors;

import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.ActorLocal;
import unibo.actor22.annotations.AnnotUtil;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
import unibo.wenvUsage22.common.ApplData;

//@Context(name = "firstCtx", host = "locahost", port = "8080")
@ActorLocal(name = "a1", implement = FirstQakActor22Fsm.class)
public  class MainFirstQakActor22Fsm  {
 
 	
	public void doJob( String name ) {
		CommSystemConfig.tracing = false;
 		//new FirstQakActor22Fsm( name ) ;
		AnnotUtil.createActorLocal(this);
		
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

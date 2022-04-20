package unibo.wenvUsage22.actors.basic;

import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
import unibo.wenvUsage22.common.ApplData;
import unibo.wenvUsage22.actors.SysData;

public class MainActorUsingWEnv {
	
	public void doJob() {
		String actorName="wenvUseActor";
		new ActorUsingWEnv(actorName);
		CommSystemConfig.tracing = false;
		Qak22Context.showActorNames();
 		//Qak22Util.sendAMsg( startCmd ); 
		Qak22Util.sendAMsg( SysData.startSysCmd("main",actorName) );
		CommUtils.delay(60000); //Give time to work ...
	};

	public static void main( String[] args) throws Exception {
		CommUtils.aboutThreads("Before start - ");
		new MainActorUsingWEnv( ).doJob();
		CommUtils.aboutThreads("Before end - ");		
	}

}

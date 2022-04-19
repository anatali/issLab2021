package unibo.wenvUsage22.actors.robot;
import it.unibo.kactor.IApplMessage;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
import unibo.wenvUsage22.common.ApplData;


public  class MainRobotMoverFsm  {
 	private IApplMessage startCmd,  haltCmd;
 	
	public MainRobotMoverFsm( String name ) {
		startCmd = CommUtils.buildDispatch("main", "activate", "activate",name );
		haltCmd  = CommUtils.buildDispatch("main", "halt", "100",name );
		
		new RobotMoverFsm( name );
		doJob(name);
		
	}
	protected void doJob(String name)  {
		CommSystemConfig.tracing = false;
		Qak22Context.showActorNames();
 		//Qak22Util.sendAMsg( startCmd ); 
		Qak22Util.sendAMsg( ApplData.w("main",name) );
		CommUtils.delay(2000);		
	}
	
	
//---------------------------------------------------------
	public static void main( String[] args) throws Exception {
		CommUtils.aboutThreads("Before start - ");
		new MainRobotMoverFsm("rm1");
		CommUtils.aboutThreads("Before end - ");		
	}
}

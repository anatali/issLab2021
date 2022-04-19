package unibo.wenvUsage22.actors.robot;
import it.unibo.kactor.IApplMessage;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.ActorLocal;
import unibo.actor22.annotations.AnnotUtil;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
import unibo.wenvUsage22.actors.FirstQakActor22Fsm;
import unibo.wenvUsage22.common.ApplData;

@ActorLocal(name = ApplData.robotName, implement = RobotMoverFsm.class)
public  class MainRobotMoverFsm  {
   	
	public MainRobotMoverFsm(   ) {	
		//new RobotMoverFsm( name );
		AnnotUtil.createActorLocal(this);
		doJob( );
		
	}
	protected void doJob( )  {
		CommSystemConfig.tracing = false;
		Qak22Context.showActorNames();
 		//Qak22Util.sendAMsg( startCmd ); 
		Qak22Util.sendAMsg( ApplData.w("main",ApplData.robotName) );
		CommUtils.delay(2000);		
	}
	
	
//---------------------------------------------------------
	public static void main( String[] args) throws Exception {
		CommUtils.aboutThreads("Before start - ");
		new MainRobotMoverFsm( ).doJob();
		CommUtils.aboutThreads("Before end - ");		
	}
}

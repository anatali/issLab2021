package unibo.wenvUsage22.cleaner.prototype0;

import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.Actor22;
import unibo.actor22.annotations.Context22;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
 
@Context22(name = "ctx", host = "localhost", port = "8020")
@Actor22(name = "basicrobot", contextName = "ctx", implement = BasicRobot.class)
public class MainBasicRobot {
	
	public static final String myName = "basicRobot";
	
	public void doJob() {
		CommSystemConfig.tracing = true;
		Qak22Context.configureTheSystem(this);
		Qak22Context.showActorNames();
  		//Qak22Util.sendAMsg( SystemData.startSysCmd("main",myName) );
	};

	public void terminate() {
		CommUtils.aboutThreads("Before end - ");		
		CommUtils.delay(60000); //Give time to work ...
		CommUtils.aboutThreads("At exit - ");		
		System.exit(0);
	}
	
	public static void main( String[] args) throws Exception {
		CommUtils.aboutThreads("Before start - ");
		MainBasicRobot appl = new MainBasicRobot( );
		appl.doJob();
		appl.terminate();
	}

}

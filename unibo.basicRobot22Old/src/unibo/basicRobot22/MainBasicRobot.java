package unibo.basicRobot22;

 
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.Actor22;
import unibo.actor22.annotations.Context22;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
 

@Context22(name = "ctx", host = "localhost", port = MainBasicRobot.port)
@Actor22(name = MainBasicRobot.robotName, contextName = "ctx", implement = BasicRobot22.class)
@Actor22(name = MainBasicRobot.callerName, contextName = "ctx", implement = RobotCmdSender.class)
public class MainBasicRobot {
	
	public static final String port       = "8037";
	public static final String callerName = "robotCmdSender";
	public static final String robotName  = "basicRobot";
	
	public void doJob() {
		CommSystemConfig.tracing = false;
		Qak22Context.configureTheSystem(this);
		Qak22Context.showActorNames();

 		//ActorObserver obs = 
 				new BasicRobotObserver(MainBasicRobot.port,robotName);


//  		Qak22Util.sendAMsg( SystemData.startSysCmd("main",robotName) );
//  		Qak22Util.sendAMsg( SystemData.startSysCmd("main",callerName) );
	};

	public void terminate() {
		CommUtils.aboutThreads("Before end - ");		
		CommUtils.delay(600000); //Give time to work ...
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

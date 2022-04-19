package unibo.wenvUsage22.actors.robot.annotated;
import unibo.actor22comm.utils.CommUtils;
import unibo.wenvUsage22.common.ApplData;


public  class MainRobotMoverFsmAnnotated  {
	
	public void configure() {
		new RobotMoverFsmAnnotated(ApplData.robotName);  //first, since must connect ...
		new RobotController( ApplData.controllerName );		
	}
	public static void main( String[] args) throws Exception {
		CommUtils.aboutThreads("Before start - ");
		new MainRobotMoverFsmAnnotated().configure();
		CommUtils.delay(2000);
		CommUtils.aboutThreads("Before end - ");		
	}
}

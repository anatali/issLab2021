/*
ClientUsingHttp.java
*/
package unibo.wenvUsage22.annot.walker;
import unibo.actor22comm.utils.CommUtils;
import unibo.actor22.Qak22Context;
import unibo.actor22.annotations.Actor;
import unibo.actor22.annotations.AnnotUtil;
import unibo.wenvUsage22.actors.robot.annotated.RobotMoverFsmAnnotated;
import unibo.wenvUsage22.common.ApplData;

@Actor(name = ApplData.robotName,      implement = BoundaryWalkerAnnot.class)
public class MainBoundaryWalkerAnnot { 
 	

	protected void configure() throws Exception {
		//new BoundaryWalkerAnnot(ApplData.robotName);
		AnnotUtil.handleRepeatableActorDeclaration(this);
		CommUtils.delay(500);  //Give time to start ...
		Qak22Context.showActorNames();
 	}
 
 /*
MAIN
 */
	public static void main(String[] args) throws Exception   {
		CommUtils.aboutThreads("Before start - ");
 		new MainBoundaryWalkerAnnot().configure();
 		CommUtils.aboutThreads("At end - ");
	}
	
 
	
 }

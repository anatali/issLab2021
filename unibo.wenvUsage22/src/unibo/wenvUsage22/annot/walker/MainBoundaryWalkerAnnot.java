/*
ClientUsingHttp.java
*/
package unibo.wenvUsage22.annot.walker;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;
import unibo.actor22comm.http.*;
import unibo.wenvUsage22.common.ApplData;

public class MainBoundaryWalkerAnnot { 
 	

	protected void configure() throws Exception {
		new BoundaryWalkerAnnot(ApplData.robotName);
 	}
 
 /*
MAIN
 */
	public static void main(String[] args) throws Exception   {
		CommUtils.aboutThreads("Before start - ");
 		new MainBoundaryWalkerAnnot().configure();
 		CommUtils.delay(20000);
		CommUtils.aboutThreads("At end - ");
	}
	
 
	
 }

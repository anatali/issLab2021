/*
===============================================================
ClientBoundaryUsingPost.java
===============================================================
*/
package it.unibo.wenv;
import it.unibo.annotations.*;
import it.unibo.interaction.*;
import it.unibo.supports.RobotApplicationStarter;

@ArilRobotSpec
public class ClientBoundaryUsingPost {
	private IssOperations rs;	//robot support

	public ClientBoundaryUsingPost(IssOperations support){  //Injected by UniboRobotApplicationStarter
		this.rs = support;
	}

	protected String doBoundary(int stepNum, String journey) {
		if (stepNum > 4) {
			return journey;
		}
		String answer = rs.requestSynch( MsgRobotUtil.wMsg );
		while( answer.equals("true") ){
			journey = journey + "w";
			answer = rs.requestSynch( MsgRobotUtil.wMsg );
		}
		//collision
		rs.requestSynch(MsgRobotUtil.lMsg);
		return doBoundary(stepNum + 1, journey + "l");
	}
/*
MAIN
 */
	public static void main(String[] args)   {
		Object appl = RobotApplicationStarter.createInstance(ClientBoundaryUsingPost.class);

		if( appl != null ) {
			String trip = ((ClientBoundaryUsingPost)appl).doBoundary(1,"");
			System.out.println("trip="+trip);
		}

	}
	
 }


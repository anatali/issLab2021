/*
ClientUsingPost.java
===============================================================
Technology-dependent application
TODO. eliminate the communication details from this level
===============================================================
*/
package unibo.wenvUsage22.basicrobot22;
 
import unibo.comm22.interfaces.IObserver;
import unibo.comm22.interfaces.Interaction2021;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommUtils;
import unibo.comm22.ws.WsConnection;
import unibo.wenvUsage22.common.ApplData;
 

import java.util.Observable;

public class ClientUsingRobot22 implements IObserver{
 
	private Interaction2021 conn;
  
	protected void doBasicMoves() throws Exception {
 		conn = WsConnection.create("localhost:8085/socket" ); //test for  webRobot22
		((WsConnection)conn).addObserver(this);
 
 		conn.forward( ApplData.turnLeft( 800  ) );
 		CommUtils.delay(1000);
 /*
 		conn.forward( ApplData.moveForward(1500) ); 
 		//La info di fine mossa viene gestita da update/2
  		//CommUtils.delay(500);
  	    //conn.forward( ApplData.turnRight(300) );  //not allowed. Send stop before 
 		
  		CommUtils.delay(500);
 		conn.forward( ApplData.stop( ) );
 
 		conn.forward( ApplData.turnRight( 300 ) );
 		CommUtils.delay(500);
 		conn.forward( ApplData.turnLeft( 300 ) );  //not allowed before 300
 		CommUtils.delay(500);
  		conn.forward( ApplData.moveBackward(1500) ); 
  		CommUtils.delay(300);
  		conn.forward( ApplData.stop( ) );
*/
 	}
	
	@Override
	public void update(Observable source, Object data) {
		ColorsOut.out("ClientUsingWs update/2 receives:" + data);
//		JSONObject d = new JSONObject(""+data);
//		ColorsOut.outappl("ClientUsingWs update/2 collision=" + d.has("collision"), ColorsOut.MAGENTA);
		
	}
	@Override
	public void update(String data) {
		ColorsOut.out("ClientUsingWs update receives:" + data);
	}	
/*
MAIN
 */
	public static void main(String[] args) throws Exception   {
		CommUtils.aboutThreads("Before start - ");
 		new ClientUsingRobot22().doBasicMoves();
		CommUtils.aboutThreads("At end - ");
	}
	

	
 }

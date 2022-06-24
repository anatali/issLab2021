/*
ClientUsingPost.java
===============================================================
Technology-dependent application
TODO. eliminate the communication details from this level
===============================================================
*/
package unibo.wenvUsage22.wshttp;
 
import unibo.comm22.interfaces.IObserver;
import unibo.comm22.interfaces.Interaction2021;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommUtils;
import unibo.comm22.ws.WsConnection;
import unibo.wenvUsage22.common.ApplData;
 

import java.util.Observable;

public class ClientUsingWs implements IObserver{
 
	private Interaction2021 conn;
  
	protected void doBasicMoves() throws Exception {
		conn = WsConnection.create("localhost:8091" );
		//conn = WsConnection.create("localhost:8085/socket" ); //test for  webRobot22
		((WsConnection)conn).addObserver(this);
 
 		ColorsOut.outappl("1 - ClientUsingWs turnLeft( 400 )" , ColorsOut.MAGENTA);
 		conn.forward( ApplData.turnLeft( 800  ) );
 		CommUtils.delay(1000);
 		ColorsOut.outappl("2 - ClientUsingWs moveForward(1500)" , ColorsOut.MAGENTA);
  		conn.forward( ApplData.moveForward(1500) ); 
 		//La info di fine mossa viene gestita da update/2
  		//CommUtils.delay(500);
  	    //conn.forward( ApplData.turnRight(300) );  //not allowed. Send stop before 
 		
  		CommUtils.delay(500);
 		ColorsOut.outappl("ClientUsingWs stop( ) " , ColorsOut.MAGENTA);
		conn.forward( ApplData.stop( ) );
 
//  		CommUtils.delay(500);
		ColorsOut.outappl("3- ClientUsingWs moveBackward(2500)" , ColorsOut.MAGENTA);
  		conn.forward( ApplData.moveBackward(2500) ); 
  		CommUtils.delay(2000);  //collision
// 		ColorsOut.outappl("ClientUsingWs stop( ) " , ColorsOut.MAGENTA);
// 		conn.forward( ApplData.stop( ) );
 		ColorsOut.outappl("4- ClientUsingWs moveForward(1500) " , ColorsOut.MAGENTA);
 		conn.forward( ApplData.moveForward(1500) ); 
  		CommUtils.delay(500);
 		ColorsOut.outappl("ClientUsingWs stop( ) " , ColorsOut.MAGENTA);
		conn.forward( ApplData.stop( ) );
		
		ColorsOut.outappl("5-ClientUsingWs moveBackward(2500)" , ColorsOut.MAGENTA);
  		conn.forward( ApplData.moveBackward(2500) );   //obstacle
  		CommUtils.delay(1000);  //DEVO ASPETTARE ALMENO fino a collision 
 		ColorsOut.outappl("6- ClientUsingWs turnRight( 300 )" , ColorsOut.MAGENTA);
 		conn.forward( ApplData.turnRight( 300 ) );
  		
//			conn.forward( turnRight(300) );
// 			CommUtils.delay(500);
//			conn.forward( stop() );
//			conn.forward( turnLeft(300) );
// 			CommUtils.delay(500);
//			conn.forward( stop() );
//			//CommUtils.delay(500);
//			conn.forward( moveForward(1000) );
 
//			CommUtils.delay(1500);
//			conn.close();
// 
	}
	
	protected void webRobot22Moves() throws Exception {
		conn = WsConnection.create("localhost:8091" );
		//conn = WsConnection.create("localhost:8085/socket" ); //test for  webRobot22
		((WsConnection)conn).addObserver(this);
		
		
 		ColorsOut.outappl("1- ClientUsingWs moveForward(1500) " , ColorsOut.MAGENTA);
 		conn.forward( ApplData.moveForward(3500) ); 
 		//Non bisogna inviare prima di una  collisione o di uno stop
		ColorsOut.outappl("2-ClientUsingWs moveBackward(2500)" , ColorsOut.MAGENTA);
  		conn.forward( ApplData.moveBackward(3500) );    
		
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
 		//new ClientUsingWs().doBasicMoves();
 		new ClientUsingWs().webRobot22Moves();
		CommUtils.aboutThreads("At end - ");
	}
	

	
 }

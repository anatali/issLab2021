/*
ClientUsingPost.java
===============================================================
Technology-dependent application
TODO. eliminate the communication details from this level
===============================================================
*/
package unibo.wenvUsage22.wshttp;
import org.json.JSONObject;
import unibo.actor22comm.interfaces.IObserver;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;
import unibo.actor22comm.http.*;
import java.util.Observable;

public class ClientUsingHttp implements IObserver{
	private  final String localHostName    = "localhost"; //"localhost"; 192.168.1.7
	private  final int port                = 8090;
	private  final String HttpURL          = "http://"+localHostName+":"+port+"/api/move";
 

	private Interaction2021 conn;
	
	protected String crilCmd(String move, int time){
		String crilCmd  = "{\"robotmove\":\"" + move + "\" , \"time\": " + time + "}";
		//ColorsOut.out( "ClientNaiveUsingPost |  buildCrilCmd:" + crilCmd );
		return crilCmd;
	}
	public String moveForward(int duration)  { return crilCmd("moveForward", duration) ; }
	public String moveBackward(int duration) { return crilCmd("moveBackward", duration); }
	public String turnLeft(int duration)     { return crilCmd("turnLeft", duration);     }
	public String turnRight(int duration)    { return crilCmd("turnRight", duration);    }
	public String stop(int duration)         { return crilCmd("alarm", duration);        }
	public String stop( )                    { return crilCmd("alarm", 10);        }

	protected void doBasicMoves() throws Exception {
  		conn = HttpConnection.create("localhost:8090" ); //INTERROMPIBILE usando WebGui
 		String answer = conn.request( turnLeft(300) );
		ColorsOut.outappl("answer= " + answer, ColorsOut.BLACK  );
		answer = conn.request( turnRight(1000) );
		ColorsOut.outappl("answer= " + answer, ColorsOut.BLACK  );
 		answer = conn.request( moveForward(2000) ); //risposta dopo duration a meno di interruzioni
		ColorsOut.outappl("answer= " + answer, ColorsOut.BLACK  );
	}
 
	protected void doBasicMovesTest() throws Exception {
		conn = HttpConnection.create("localhost:8090" );
		
 		
			ColorsOut.out("STARTING doBasicMoves ... ");
			boolean endmove = false;
			
//			conn.forward( turnRight(1300) );
// 			CommUtils.delay(500);
//			conn.forward( stop() );
//			conn.forward( turnLeft(300) );
// 			CommUtils.delay(200);
//			conn.forward( stop() );
			//CommUtils.delay(500);
			conn.forward( moveForward(500) );
			
//			endmove = requestSynch( turnLeft(300) );
//			ColorsOut.out("turnLeft endmove=" + endmove);
//			endmove = requestSynch( turnRight(300) );
//			ColorsOut.out("turnRight endmove=" + endmove);
//
//			//Now the value of endmove depends on the position of the robot
//			endmove = requestSynch( moveForward(1800) );
//			ColorsOut.out("moveForward endmove=" + endmove);
//			endmove = requestSynch( moveBackward(800) );
			CommUtils.delay(1500);
			conn.close();
 
	}
/*
MAIN
 */
	public static void main(String[] args) throws Exception   {
		CommUtils.aboutThreads("Before start - ");
 		new ClientUsingHttp().doBasicMoves();
		CommUtils.aboutThreads("At end - ");
	}
	
	@Override
	public void update(Observable source, Object data) {
		ColorsOut.out("ClientUsingWsHttp update/2 receives:" + data);
		JSONObject d = new JSONObject(""+data);
		ColorsOut.outappl("ClientUsingWsHttp update/2 collision=" + d.has("collision"), ColorsOut.MAGENTA);
		
	}
	@Override
	public void update(String data) {
		ColorsOut.out("ClientUsingWsHttp update receives:" + data);
	}
	
 }

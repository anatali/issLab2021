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
import unibo.actor22comm.ws.*;
import java.util.Observable;

public class ClientUsingWs implements IObserver{
 
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

	protected void doBasicMovesTest() throws Exception {
		conn = WsConnection.create("localhost:8091" );
		((WsConnection)conn).addObserver(this);
		conn.forward( moveForward(2300) );
	}
 
	protected void doBasicMoves() throws Exception {
		conn = WsConnection.create("localhost:8091" );
		((WsConnection)conn).addObserver(this);
		boolean endmove = false;
			 
			conn.forward( turnRight(300) );
 			CommUtils.delay(500);
			conn.forward( stop() );
			conn.forward( turnLeft(300) );
 			CommUtils.delay(500);
			conn.forward( stop() );
			//CommUtils.delay(500);
			conn.forward( moveForward(1000) );
 
			CommUtils.delay(1500);
			conn.close();
 
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
/*
MAIN
 */
	public static void main(String[] args) throws Exception   {
		CommUtils.aboutThreads("Before start - ");
 		new ClientUsingWs().doBasicMoves();
		CommUtils.aboutThreads("At end - ");
	}
	

	
 }

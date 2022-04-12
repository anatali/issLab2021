/*
ClientUsingPost.java
===============================================================
Technology-dependent application
TODO. eliminate the communication details from this level
===============================================================
*/
package unibo.wenvUsage22.wshttp;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import it.unibo.is.interfaces.IObserver;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;
import unibo.actor22comm.wshttp.WsHttpConnection;

import java.net.URI;
import java.util.Observable;

public class ClientUsingWsHttp implements IObserver{
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
		//conn = WsHttpConnection.createForWs("localhost:8091" );
		conn = WsHttpConnection.createForHttp("localhost:8090" );
		
		
		((WsHttpConnection)conn).addObserver(this);
		
			ColorsOut.out("STARTING doBasicMoves ... ");
			boolean endmove = false;
			
			conn.forward( turnLeft(300) );
			//conn.forward( stop() );
			CommUtils.delay(500);
			conn.forward( turnRight(300) );
			
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
 		new ClientUsingWsHttp().doBasicMoves();
		CommUtils.aboutThreads("At end - ");
	}
	
	@Override
	public void update(Observable source, Object data) {
		ColorsOut.out("ClientUsingWsHttp update receives:" + data);
		
	}
	
 }

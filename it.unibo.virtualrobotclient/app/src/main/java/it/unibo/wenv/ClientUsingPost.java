/*
===============================================================
ClientUsingPost.java
===============================================================
*/
package it.unibo.wenv;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.net.URI;

public class ClientUsingPost {
	private  final String localHostName    = "localhost"; //"localhost"; 192.168.1.7
	private  final int port                = 8090;
	private  final String URL              = "http://"+localHostName+":"+port+"/api/move";
	private  final String containerHostName= "wenv";

	public static int finalNumOfSteps      = 0;
	public ClientUsingPost() { }

  	protected boolean sendCmd(String move, int time)  {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			System.out.println( move + " sendCmd "  );
			//String json         = "{\"robotmove\":\"" + move + "\"}";
			String json         = "{\"robotmove\":\"" + move + "\" , \"time\": " + time + "}";
			StringEntity entity = new StringEntity(json);
			HttpUriRequest httppost = RequestBuilder.post()
					.setUri(new URI(URL))
					.setHeader("Content-Type", "application/json")
					.setHeader("Accept", "application/json")
					.setEntity(entity)
					.build();
			CloseableHttpResponse response = httpclient.execute(httppost);
			//System.out.println( "ClientUsingPost | sendCmd response= " + response );
			boolean collision = checkCollision(response);
			//boolean collision = checkCollision_javax(response);
			return collision;
		} catch(Exception e){
			System.out.println("ERROR:" + e.getMessage());
			return true;
		}
	}

	protected boolean checkCollision(CloseableHttpResponse response) throws Exception {
		try{
			//response.getEntity().getContent() is an InputStream
			String jsonStr = EntityUtils.toString( response.getEntity() );
			System.out.println( "ClientUsingPost | checkCollision_simple jsonStr= " +  jsonStr );
			//jsonStr = {"endmove":true,"move":"moveForward"}
			//org.json.simple.parser.JSONParser simpleparser = new JSONParser();
			//org.json.simple.JSONObject jsonObj              = (JSONObject) simpleparser.parse( jsonStr );
			JSONObject jsonObj = new JSONObject(jsonStr) ;
			boolean collision = false;
			if( jsonObj.get("endmove") != null ) {
				collision = ! jsonObj.get("endmove").toString().equals("true");
				System.out.println("ClientUsingPost | checkCollision_simple collision=" + collision);
			}
			return collision;
		}catch(Exception e){
			System.out.println("ClientUsingPost | checkCollision_simple ERROR:" + e.getMessage());
			throw(e);
		}
	}

	public boolean moveForward(int duration)  { return sendCmd("moveForward", duration);  }
	public boolean moveBackward(int duration) { return sendCmd("moveBackward", duration); }
	public boolean moveLeft(int duration)     { return sendCmd("turnLeft", duration);     }
	public boolean moveRight(int duration)    { return sendCmd("turnRight", duration);    }
	public boolean moveStop(int duration)     { return sendCmd("alarm", duration);        }

	protected int boundary() {
		int nStep = 0;
		try {
			System.out.println("STARTING boundary ... ");
			for( int i = 1; i<=4 ; i++ ) {
				boolean b = false;
				while (!b ) {
					b = moveForward(600);
					Thread.sleep(300);
				}
				nStep++;
 				moveLeft(300);
				Thread.sleep(300);
			}
			//Thread.sleep(3000);	//avoids premature termination (in docker-compose)
			System.out.println("END nStep=" + nStep );
			return nStep;
		} catch (Exception e) {
			System.out.println( "ERROR " + e.getMessage());
			return -1;
		}

	}
	protected void doMoves() {
		try {
			boolean collision = false;
			collision = moveLeft(200);
			System.out.println("ClientUsingPost | moveLeft collision=" + collision);
			collision = moveRight(200);
			System.out.println("ClientUsingPost | moveRight collision=" + collision);
			collision = moveForward(800);
			System.out.println("ClientUsingPost | moveForward collision=" + collision);
			collision = moveStop(100);
			System.out.println("ClientUsingPost | moveStop collision=" + collision);
 			collision = moveBackward(800);
			System.out.println("ClientUsingPost | moveForward moveBackward=" + collision);
		}catch (Exception e) {
			System.out.println( "ERROR " + e.getMessage());
		}
	}

/*
	protected void doForwardBackward() {
		try {
			System.out.println("STARTING testForwardBackward ... ");
			boolean collision = false;
			while( ! collision ) {
				Thread.sleep(500);
				collision = moveForward(600);
			}
			//Return to home
			Thread.sleep(1000);
			collision = false;
			while( ! collision ) {
				collision = moveBackward(600);
				Thread.sleep(500);
			}
			System.out.println("END testForwardBackward");
		}catch (Exception e) {
			System.out.println( "ERROR " + e.getMessage());
		}
	}
	protected boolean doMoveLeft() {
		try {
			System.out.println("ClientUsingPost | STARTING doMoveLeft ... ");
			boolean moveFailed = moveLeft(300);
 			System.out.println("ClientUsingPost | END moveFailed=" + moveFailed);
 			return moveFailed;
		}catch (Exception e) {
			System.out.println( "ClientUsingPost | ERROR " + e.getMessage());
			return false;
		}
	}
*/
/*
MAIN
 */
	public static void main(String[] args)   {
		//new ClientUsingPost().doMoveLeft();
		//new ClientUsingPost().doForwardBackward();
		new ClientUsingPost().doMoves();
		//finalNumOfSteps = new ClientUsingPost().boundary();
		//System.out.println("boundary finalNumOfSteps="+finalNumOfSteps);
	}
	
 }


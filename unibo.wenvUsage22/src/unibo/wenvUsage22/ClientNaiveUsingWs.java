/**
 * ClientNaiveUsingWs
 ===============================================================
 * Technology-dependent application
 * TODO. eliminate the communication details from this level
 ===============================================================
 */

package unibo.wenvUsage22;
import javax.websocket.*; 
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONObject;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;

@ClientEndpoint
public class ClientNaiveUsingWs {
    private Session userSession    = null;

    public ClientNaiveUsingWs(String addr) {
            ColorsOut.out("ClientNaiveUsingWs |  CREATING ..." + addr);
            init(addr);
    }

    protected void init(String addr){
        try {
            //ColorsOut.out("ClientNaiveUsingWs |  container=" + ContainerProvider.class.getName());
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://"+addr));
        } catch (URISyntaxException ex) {
            System.err.println("ClientNaiveUsingWs | URISyntaxException exception: " + ex.getMessage());
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        ColorsOut.out("ClientNaiveUsingWs | opening websocket");
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        ColorsOut.out("ClientNaiveUsingWs | closing websocket");
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message)  {
        try {
            //{"collision":"true ","move":"..."} or {"sonarName":"sonar2","distance":19,"axis":"x"}
            ColorsOut.out("ClientNaiveUsingWs | onMessage:" + message);
            JSONObject jsonObj = new org.json.JSONObject(message);
            if (jsonObj.get("endmove") != null) {
                String endmove = jsonObj.getString("endmove");
                String  move   = jsonObj.get("move").toString();
                ColorsOut.out("ClientNaiveUsingWs | onMessage " + move + " endmove=" + endmove);
            } else if (jsonObj.get("collision") != null) {
                boolean collision = jsonObj.get("collision").toString().equals("true");
                String move = jsonObj.get("move").toString();
                ColorsOut.out("ClientNaiveUsingWs | onMessage collision=" + collision + " move=" + move);
             } else if (jsonObj.get("sonarName") != null) {
                String sonarNAme = jsonObj.get("sonarName").toString();
                String distance = jsonObj.get("distance").toString();
                ColorsOut.out("ClientNaiveUsingWs | onMessage sonaraAme=" + sonarNAme + " distance=" + distance);
            }
        } catch (Exception e) {
        	ColorsOut.outerr("onMessage ERROR for " + message);
        }

    }

/*
BUSINESS LOGIC
 */
    protected String crilCmd(String move, int time){
        String crilCmd  = "{\"robotmove\":\"" + move + "\" , \"time\": " + time + "}";
        //ColorsOut.out( "ClientNaiveUsingPost |  buildCrilCmd:" + crilCmd );
        return crilCmd;
    }

    public String moveForward(int duration)  { return crilCmd("moveForward", duration) ;  }
    public String moveBackward(int duration) { return crilCmd("moveBackward", duration); }
    public String turnLeft(int duration)     { return crilCmd("turnLeft", duration);     }
    public String turnRight(int duration)    { return crilCmd("turnRight", duration);    }
    public String stop(int duration)         { return crilCmd("alarm", duration);        }

    protected void request( String crilCmd ) throws Exception  {
        //ColorsOut.out("ClientNaiveUsingWs | request " + crilCmd);
        //this.userSession.getAsyncRemote().sendText(crilCmd);
        this.userSession.getBasicRemote().sendText(crilCmd);    
        //synch: blocks until the message has been transmitted
    }
    protected void requestDelayed( String crilCmd ) throws Exception  {
        ColorsOut.out("ClientNaiveUsingWs | requestDelayed " + crilCmd);
        request(crilCmd);
        JSONObject jsonObj = new JSONObject( crilCmd );
        int moveTime       = jsonObj.getInt("time");
        Thread.sleep( moveTime );
        ColorsOut.out("ClientNaiveUsingWs | requestDelayed resumes"  );
    	request( stop(10) );
    }

//    protected void doBasicMoves() throws Exception{
//        request( turnLeft(300) );
//        request( turnRight(300) );
//        request( moveForward(800) );
//        request( moveBackward(800) );
//        Thread.sleep(300); //move time of the turnLeft
//     }

    protected void doBasicMovesDelayed() throws Exception{
         requestDelayed( turnLeft(300) );
         requestDelayed( turnRight(300) );
         requestDelayed( moveForward(  800) );
         requestDelayed( moveBackward( 800) );
//        this.userSession = null;
    }

/*
MAIN
 */
    public static void main(String[] args) {
        try{
    		CommUtils.aboutThreads("Before start - ");
            ClientNaiveUsingWs appl = new ClientNaiveUsingWs("localhost:8091");
            //appl.doBasicMoves();
            appl.doBasicMovesDelayed();
            // give time to receive messages from websocket
            //Thread.sleep(2000);
    		CommUtils.aboutThreads("At end - ");
        } catch( Exception ex ) {
            ColorsOut.outerr("ClientNaiveUsingWs | InterruptedException exception: " + ex.getMessage());
        }
    }
}

/*
ClientNaiveUsingWs | main start n_Threads=1
ClientNaiveUsingWs |  CREATING ...
ClientNaiveUsingWs | opening websocket
ClientNaiveUsingWs | onMessage turnRight endmove=notallowed
ClientNaiveUsingWs | onMessage moveForward endmove=notallowed
ClientNaiveUsingWs | onMessage moveBackward endmove=notallowed
ClientNaiveUsingWs | onMessage turnLeft endmove=true
ClientNaiveUsingWs | onMessage turnLeft endmove=true
ClientNaiveUsingWs | onMessage turnRight endmove=true
ClientNaiveUsingWs | onMessage moveForward endmove=true
ClientNaiveUsingWs | appl  n_Threads=4
ClientNaiveUsingWs | onMessage moveBackward endmove=true
 */
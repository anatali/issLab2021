/**
ClientWebsockJavax.java
===============================================================
Solve the boundaryWalk problem
===============================================================
*/
package it.unibo.boundaryWalk;
import org.json.JSONObject;
import javax.websocket.*;
import java.net.URI;
import java.net.URISyntaxException;

@ClientEndpoint     //javax.websocket annotation
public class ClientWebsockJavax {

    public static interface SocketObserver {
        public void handleMessage(String message)  ;
    }
    public static interface Walker  {
        public void nextStep( boolean collision ) throws Exception;
    }

    private SocketObserver sockObserver;
    private Session userSession    = null;
    private int stepNum            = 0;
    private String forwardMsg      = "{\"robotmove\":\"moveForward\" , \"time\": 600}";
    private String turnLeftMsg     = "{\"robotmove\":\"turnLeft\"    , \"time\": 300}";
    private String journey         = "";        //for TESTING

    public ClientWebsockJavax(String addr) {
        System.out.println("ClientWebsockJavax |  CREATING ...");
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://"+addr));
        } catch (URISyntaxException ex) {
            System.err.println("ClientWebsockJavax | URISyntaxException exception: " + ex.getMessage());
        } catch (Exception e) {
        }
    }

    public String getJourney(){
        return journey;
    }

    //Callback hook for Connection open events.
    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("ClientWebsockJavax | opening websocket");
        this.userSession = userSession;
    }

    //Callback hook for Connection close events.
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("ClientWebsockJavax | closing websocket");
        this.userSession = null;
    }

    //Callback hook for Message Events. This method will be invoked when a client send a message.
    @OnMessage
    public void onMessage(String message)   {
        if (this.sockObserver != null) {
            this.sockObserver.handleMessage(message);
        }
    }

    //register message handler
    public void addMessageHandler(SocketObserver msgHandler) {
        this.sockObserver = msgHandler;
    }

     //Send a message.
     public void sendMessage( String message ) throws Exception {
        System.out.println("        ClientWebsockJavax | sendMessage " + message);
        //this.userSession.getAsyncRemote().sendText(message);
        this.userSession.getBasicRemote().sendText(message);    //synch: blocks until the message has been transmitted
    }

    /*
        BUSINESS LOGIC (called by sockObserver)
        For 4 times: Go ahead until the robot hits a wall. Then turn left
     */
    private Walker robotWalker = new Walker(){
        @Override
        public void nextStep( boolean endmove ) throws Exception {
                System.out.println(" %%% nextStep endmove=" + endmove + " count=" + stepNum);
                if (stepNum > 4) {
                    System.out.println("Walker | BYE (from nextStep) journey=" + journey);
                    return;
                }
                Thread.sleep(300) ;   //interval before the next move
                //System.in.read();         //To help debugging
                if( endmove ) { //ok
                    journey = journey+"w";
                    sendMessage( forwardMsg) ;
                } else {  //collision
                    stepNum++;
                    journey = journey+"l";
                    sendMessage( turnLeftMsg );
                };
            }//nextStep
        };  //Walker

    public void addObserver() {
        //Reacts to messages sent by WEnv over the socket
        addMessageHandler( new SocketObserver() {
            public void handleMessage(String message) {
                try {
                    //{"collision":"true ","move":"..."} or {"sonarName":"sonar2","distance":19,"axis":"x"}
                    System.out.println("        SocketObserver | " + message);
                    JSONObject jsonObj = new JSONObject(message) ;
                    if (jsonObj.get("endmove") != null) {
                        boolean endmove = jsonObj.getBoolean("endmove");
                        System.out.println("        SocketObserver |  endmove=" + endmove);
                        robotWalker.nextStep(endmove);
                    } else if (jsonObj.get("collision") != null) {
                        boolean collision = jsonObj.getBoolean("collision");
                        System.out.println("        SocketObserver | collision=" + collision );
                        //if( ! move.equals("unknown") ) goon.nextStep(collision);
                    } else if (jsonObj.get("sonarName") != null) {
                        String sonarName = jsonObj.getString( "sonarName");
                        String distance = jsonObj.getString("distance");
                        System.out.println("        SocketObserver | sonarName=" + sonarName + " distance=" + distance);
                    }
                } catch (Exception e) {}
            }
        });
    }
    public String doBoundaryWalk() throws Exception {
        addObserver();
        //Start the journey
        stepNum = 1;
        journey = "w";
        sendMessage( forwardMsg );
        //avoid premature exit. TODO: replace with somwthing better ...
        Thread.sleep(15000);
        //System.out.println("ClientWebsockJavax | starting ENDS ==== " );
        return journey;
    }
/*
MAIN
 */
    public static void main(String[] args) {
        try{
            String result = new ClientWebsockJavax("localhost:8091").doBoundaryWalk();
            System.out.println("ClientWebsockJavax | result=" + result);
         } catch( Exception ex ) {
            System.err.println("ClientWebsockJavax | InterruptedException exception: " + ex.getMessage());
        }
    }

}
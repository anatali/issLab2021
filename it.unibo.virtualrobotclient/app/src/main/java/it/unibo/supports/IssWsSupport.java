/**
 IssWsSupport.java
 ===============================================================
     See also AnswerAvailable
 ===============================================================
 */
package it.unibo.supports;

import it.unibo.interaction.IssObserver;
import it.unibo.interaction.IssOperations;
import org.json.JSONObject;

import javax.websocket.*;
import java.net.URI;
import java.security.Principal;
import java.util.Vector;

/**
 IssWsSupport.java
 ===============================================================
 Support for WS interaction with a remote server
 The correct format of the arguments of operations forward/request
 must be provided by the user
 ===============================================================
 */
@ClientEndpoint     //javax.websocket annotation
public class IssWsSupport implements IssOperations {
    private  String URL            = "unknown";
    private Session userSession    = null;
    private AnswerAvailable answerSupport;

    private Vector<IssObserver> observers = new Vector<IssObserver>();

    public IssWsSupport( String url ){
        try {
            URL = url;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://"+url));
            answerSupport = new AnswerAvailable();
        }catch (Exception e) {
            System.out.println("IssWsSupport | ERROR: " + e.getMessage());
        }
    }

    //Callback hook for Connection open events.
    @OnOpen
    public void onOpen(Session userSession) { //, @PathParam("username") String username, EndpointConfig epConfig
        //ClientEndpointConfig clientConfig = (ClientEndpointConfig) epConfig;
        Principal userPrincipal = userSession.getUserPrincipal();
        System.out.println("        IssWsSupport | onOpen userPrincipal=" + userPrincipal );
        if( userPrincipal != null )  { //there is an authenticated user
            System.out.println("        IssWsSupport | onOpen user=" + userPrincipal.getName());
        }
        this.userSession = userSession;
    }

    //Callback hook for Connection close events.
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("IssWsSupport | closing websocket");
        this.userSession = null;
    }

    //Callback hook for Message Events.
    //THe WENv system sends always an answer for any command sent to it
    @OnMessage
    public void onMessage(String message)   {
        try {
             //{"collision":"true ","move":"..."} or {"sonarName":"sonar2","distance":19,"axis":"x"}
            System.out.println("        IssWsSupport | onMessage:" + message);
            JSONObject jsonObj = new JSONObject(message) ;
            if (jsonObj.get("endmove") != null) {
                //HANDLE THE ANSWER
                String endmove = jsonObj.getString("endmove");
                String  move   = jsonObj.getString("move");
                if( ! endmove.equals("notallowed") )  answerSupport.put(endmove, move);
                //System.out.println("        IssWsSupport | onMessage endmove=" + endmove);
            } else if (jsonObj.get("collision") != null) {
                boolean collision = jsonObj.getBoolean("collision");
                //System.out.println("        IssWsSupport | onMessage collision=" + collision );
            } else if (jsonObj.get("sonarName") != null) {
                String sonarName = jsonObj.getString( "sonarName");
                String distance  = jsonObj.getString("distance");
                System.out.println("        IssWsSupport | onMessage sonarName=" + sonarName + " distance=" + distance);
            }
            updateObservers( jsonObj );
        } catch (Exception e) {}
    }

    @OnError
    public void disconnected(Session session, Throwable error){
        System.out.println("IssWsSupport | disconnected  " + error.getMessage());
    }

    protected void updateObservers(JSONObject jsonOnj ){
        observers.forEach( v -> v.handleInfo(jsonOnj) );
    }
    
//------------------------------ IssOperations ----------------------------------
     @Override
    public void forward(String msg)  {
        try {
             //this.userSession.getAsyncRemote().sendText(message);
            userSession.getBasicRemote().sendText(msg); //synch: blocks until the message has been transmitted
            //System.out.println("        IssWsSupport | DONE forward " + msg);
        }catch( Exception e){
            System.out.println("        IssWsSupport | ERROR forward  " + e.getMessage());
        }
    }

    @Override
    public void request(String msg) {
        try{
            //System.out.println("        IssWsSupport | request " + msg);
            //this.userSession.getAsyncRemote().sendText(message);
            this.userSession.getBasicRemote().sendText(msg);    //synch: blocks until the message has been transmitted
        }catch( Exception e){
            System.out.println("        IssWsSupport | request ERROR " + e.getMessage());
        }
    }
    @Override
    public String requestSynch(String msg) {
        try{
            //System.out.println("        IssWsSupport | requestSynch " + msg);
            //this.userSession.getAsyncRemote().sendText(message);
            request(msg);
            //WAIT for the answer (reply) received by onMessage
            //answerSupport.engage();   //OVERCOME: see version 2.0 of virtualrobot
            return answerSupport.get(); //wait for the answer
        }catch( Exception e){
            System.out.println("        IssWsSupport | request ERROR " + e.getMessage());
            return "error";
        }
    }

    @Override
    public void reply(String msg) {
        //System.out.println( "         IssWsSupport | WARNING: reply NOT IMPLEMENTED HERE"  );
    }

    @Override
    public void registerObserver( IssObserver obs ){
        observers.add( obs );
    }

    @Override
    public void removeObserver( IssObserver obs ){
        observers.remove( obs );
    }
}
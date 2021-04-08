/*
============================================================
IssWsHttpJavaSupport.java
Since it extends WebSocketListener, this class can be modelled
as an actor that is able to interact with other actors
registered as abservers
============================================================
 */
package it.unibo.supports2021;

import it.unibo.interaction.IJavaActor;
import it.unibo.interaction.IssActorObservable;
import it.unibo.interaction.IssCommSupport;
import it.unibo.interaction.IssOperations;
import okhttp3.*;
import okhttp3.internal.http.RealResponseBody;
import org.jetbrains.annotations.NotNull;

import java.util.Vector;

public class IssWsHttpJavaSupport extends WebSocketListener
        implements IssActorObservable, IssCommSupport, IssOperations {
    private boolean connectForWs               = true;
    private Vector<IJavaActor> actorobservers  = new Vector<IJavaActor>();
    private WebSocket myWs;
    private OkHttpClient okHttpClient  = new OkHttpClient();
    final MediaType JSON_MediaType     = MediaType.get("application/json; charset=utf-8");
    private String httpAddr ;
    private String wsAddr ;
    private boolean opened = false;

    public static it.unibo.supports2021.IssWsHttpJavaSupport createForHttp(String addr ){
        return new it.unibo.supports2021.IssWsHttpJavaSupport(addr, false);
    }
    public static it.unibo.supports2021.IssWsHttpJavaSupport createForWs(String addr ){
        return new it.unibo.supports2021.IssWsHttpJavaSupport(addr, true);
    }
    //Constructor
    private IssWsHttpJavaSupport(String addr, boolean wsconn ){  //localhost:8091
        connectForWs = wsconn;
        if( wsconn ) wsconnect(addr);
        else httpconnect(addr);
    }
    //----------------------------------------------------------------------

    @Override
    public void registerActor(@NotNull IJavaActor actorObs) {
        actorobservers.add(actorObs);
    }
    @Override
    public void removeActor(@NotNull IJavaActor actorObs) {
        actorobservers.remove(actorObs);
    }
    @Override
    public boolean isOpen() {
        return opened;
    }
    @Override
    public void close() {
        if( myWs != null ){
            boolean gracefulShutdown = myWs.close(1000, "appl_terminated");
            System.out.println("IssWsHttpJavaSupport | close gracefulShutdown=" + gracefulShutdown);
        }
    }

    //----------------------------------------------------------------------

    public void forward(@NotNull String msgJson) {
        if(connectForWs) myWs.send(msgJson);
        else System.out.println("SORRY: not connected for ws");
    }

    @Override
    public void request(@NotNull String msgJson) {
        if(connectForWs) myWs.send(msgJson);
        else System.out.println("SORRY: not connected for ws");
    }

    @Override
    public void reply(@NotNull String msgJson) {
        if(connectForWs) myWs.send(msgJson);
        else System.out.println("SORRY: not connected for ws");
    }

    @NotNull
    @Override
    public String requestSynch(@NotNull String msg) {
        if( ! connectForWs)  return sendHttp( msg );
        else return "SORRY: not connected for HTTP";
    }

//----------------------------------------------------------------------
    @Override
    public void onOpen(WebSocket webSocket, Response response  ) {
        System.out.println("IssWsHttpJavaSupport | onOpen ");
        opened = true;
    }
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason  ) {
        System.out.println("IssWsHttpJavaSupport | onClosing ");
    }
    @Override
    public void onMessage(WebSocket webSocket, String msg  ) {
        //System.out.println("IssWsHttpJavaSupport | onMessage " + msg );
        updateObservers( msg );
    }

    protected void updateObservers( String msg ){
        //System.out.println("IssWsHttpJavaSupport | updateObservers " + observers.size() );
         actorobservers.forEach( v -> v.send( msg ) );
    }

//----------------------------------------------------------------------
    public void wsconnect(String wsAddr){    // localhost:8091
        this.wsAddr = wsAddr;
        Request request = new Request.Builder()
                .url( "ws://"+wsAddr )
                .build() ;
        myWs = okHttpClient.newWebSocket(request, this);
        System.out.println("IssWsHttpJavaSupport | wsconnect myWs=" + myWs);
    }

    public void httpconnect(String httpaddr){    //localhost:8090/api/move
        this.httpAddr = httpaddr; /*
        try {
        Request request = new Request.Builder()
                .url( "http://"+httpaddr )
                .build() ;
        Response response = okHttpClient.newCall(request).execute(); //a stream
        String answer     = ((RealResponseBody) response.body()).string();
        System.out.println("IssWsHttpJavaSupport | httpconnect answer=" + answer);
        }catch(Exception e){
             e.printStackTrace();
        }*/
    }

    public String sendHttp( String msgJson){
        try {
            System.out.println("IssWsHttpJavaSupport | sendHttp httpAddr=" + httpAddr);
            RequestBody body = RequestBody.create(JSON_MediaType, msgJson);
            Request request = new Request.Builder()
                    .url( "http://"+httpAddr+"/api/move" )   //TODO
                    .post(body)
                    .build();
            Response response = okHttpClient.newCall(request).execute(); //a stream
            String answer     = ((RealResponseBody) response.body()).string();
            System.out.println("IssWsHttpJavaSupport | response body=" + answer);
            return answer;
        }catch(Exception e){
            return "";
        }
    }


}

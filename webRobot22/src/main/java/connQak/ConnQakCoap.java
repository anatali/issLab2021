package connQak;

import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import unibo.actor22comm.coap.CoapConnection;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import org.eclipse.californium.core.CoapClient;

public class ConnQakCoap extends ConnQakBase { //implements Interaction2021
    private Interaction2021 conn;
    private CoapClient client;
    private String hostAddr;
    private int port;

    @Override
    public Interaction2021 createConnection(String hostAddr, int port){
        try {
            this.hostAddr = hostAddr;
            this.port     = port;
            String url = "coap://"+hostAddr+":"+port+"/"+ ctxqakdest+"/"+qakdestination;
            ColorsOut.outappl("connQakCoap | createConnection url="+url, ColorsOut.YELLOW);
            client = new CoapClient( url );
            client.setTimeout( 1000L );
            //initialCmd: to make console more reactive at the first user cmd
            CoapResponse respGet  = client.get( ); //CoapResponse
            if( respGet != null )
                ColorsOut.outappl("connQakCoap | createConnection doing  get | CODE=  "
                        + respGet.getCode(), ColorsOut.YELLOW);
            else
                ColorsOut.outappl("connQakCoap | url=  " + url
                        + " FAILURE", ColorsOut.YELLOW);
            conn = new CoapConnection(hostAddr,port);
            return conn;
        } catch (Exception e) {
            ColorsOut.outerr("connQakCoap | createConnection ERROR:" + e.getMessage() );
            return null;
        }
    }

    @Override
    public void forward(String msg) {
        try {
            ColorsOut.outappl("doMove:" + msg, ColorsOut.YELLOW);
            client.put(msg, MediaTypeRegistry.TEXT_PLAIN);
        } catch (Exception e) {
            ColorsOut.outerr("connQakCoap | forward ERROR:" + e.getMessage() );
        }
    }

    @Override
    public void request(String msg) {
        try {
            CoapResponse respPut = client.put(msg, MediaTypeRegistry.TEXT_PLAIN);
        if( respPut != null )
            ColorsOut.outappl("connQakCoap | answer="+ respPut.getResponseText(), ColorsOut.YELLOW);
        } catch (Exception e) {
            ColorsOut.outerr("connQakCoap | request ERROR:" + e.getMessage() );
        }

    }

    @Override
    public void emit(String msg) {
        try {
           String url = "coap://"+hostAddr+":"+port+"/"+ ctxqakdest ;
            client = new CoapClient( url );
            //println("PUT emit url=${url} ")
            CoapResponse respPut = client.put(msg.toString(), MediaTypeRegistry.TEXT_PLAIN);
            ColorsOut.outappl("connQakCoap | PUT emit ${msg} RESPONSE CODE=  ${respPut.code}", ColorsOut.YELLOW);
        } catch (Exception e) {
            ColorsOut.outerr("connQakCoap | request ERROR:" + e.getMessage() );
        }
    }

 }

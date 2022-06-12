package connQak;

import unibo.actor22comm.ProtocolType;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;

public abstract class ConnQakBase  {

    private static ConnQakBase currQakConn;
    public static final String ctxqakdest      = "ctxbasicrobot";
    public static final String qakdestination 	= "basicrobot";

    public static ConnQakBase create(ProtocolType protocol) {
        ColorsOut.outappl("ConnQakBase | create protocol="+protocol, ColorsOut.MAGENTA);

        if( protocol == ProtocolType.tcp ){
            currQakConn = new ConnQakTcp( );
            return currQakConn;
        } else if( protocol == ProtocolType.coap ){
            currQakConn = new ConnQakCoap( );
            return currQakConn;
        }
        else return null;
    }

    public abstract Interaction2021 createConnection(String host, int port  );
    public abstract void forward(String msg);
    public abstract void request(String msg);
    public abstract void emit(String msg);



}
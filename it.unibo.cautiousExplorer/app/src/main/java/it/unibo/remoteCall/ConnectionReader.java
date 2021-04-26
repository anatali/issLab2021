/*
============================================================
ConnectionReader

============================================================
 */
package it.unibo.remoteCall;

import it.unibo.actor0.ApplMessage;
import it.unibo.interaction.IUniboActor;
import it.unibo.is.interfaces.protocols.IConnInteraction;
import it.unibo.supports2021.ActorBasicJava;
import org.jetbrains.annotations.NotNull;

public class ConnectionReader extends ActorBasicJava  {
    private IConnInteraction conn;

    public ConnectionReader(String name, IConnInteraction conn) {
        super(name);
        this.conn = conn;
    }

    protected void getInput(ConnectionReader obj) throws Exception {
        while (true) {
            //System.out.println("ConnectionReader  | waitInput ... "   );
            String v = conn.receiveALine();
            //System.out.println("ConnectionReader  | getInput " + v );
            if (v != null) {
                String msg = ApplMessage.create(v).toString();
                //System.out.println("ConnectionReader  | updateObservers " + msg );
                updateObservers(msg);
            }else{
                break;
            }
        }//while
    }


    @Override
    protected void handleInput(String s) {
        try {
            //System.out.println("ConnectionReader handleInput:" + s);
            ApplMessage msg = ApplMessage.create(s);
            //System.out.println("ConnectionReader handleInput msg=" + msg.getMsgId());
            if (msg.getMsgId().equals("start")) {
                getInput( this );
            }
        }catch( Exception e){
            System.out.println("ConnectionReader ERROR:" + e.getMessage());
        }

    }//handleInput

    @Override
    public void registerActor(@NotNull IUniboActor iUniboActor) {

    }

    @Override
    public void removeActor(@NotNull IUniboActor iUniboActor) {

    }
}

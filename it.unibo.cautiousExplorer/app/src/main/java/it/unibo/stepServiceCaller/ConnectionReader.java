package it.unibo.stepServiceCaller;

import it.unibo.actor0.ApplMessage;
import it.unibo.actor0.sysUtil;
import it.unibo.is.interfaces.protocols.IConnInteraction;
import it.unibo.supports2021.ActorBasicJava;

public class ConnectionReader extends ActorBasicJava  {
    private IConnInteraction conn;

    public ConnectionReader(String name, IConnInteraction conn) {
        super(name);
        this.conn = conn;
    }

    protected void getInput(ConnectionReader obj) throws Exception {
        while (true) {
            String v = conn.receiveALine();
            System.out.println("ConnectionReader  | getInput " + v );
            if (v != null) {
                String msg = ApplMessage.create(v).toString();
                obj.updateObservers(msg);
            }else{
                break;
            }
        }//while
    }


    @Override
    protected void handleInput(String s) {
        try {
            System.out.println("ConnectionReader handleInput:" + s);
            ApplMessage msg = ApplMessage.create(s);
            System.out.println("ConnectionReader handleInput msg=" + msg.getMsgId());
            if (msg.getMsgId().equals("start")) {
                getInput( this );
            }
        }catch( Exception e){
            System.out.println("ConnectionReader ERROR:" + e.getMessage());
        }

    }//handleInput

}

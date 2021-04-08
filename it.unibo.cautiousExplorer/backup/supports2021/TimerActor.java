package it.unibo.supports2021;

import it.unibo.interaction.IJavaActor;
import it.unibo.supports2021.ActorBasicJava;
import it.unibo.supports2021.ActorMsgs;
import org.json.JSONObject;


public class TimerActor extends ActorBasicJava{
    //private long tout          = 0;
    private IJavaActor owner   = null;
    private boolean killed     = false;

    public TimerActor(String name, IJavaActor owner) {
        super(name);
        //this.tout  = tout;
        this.owner = owner;
    }

    @Override
    protected void handleInput(String msgJsonStr) {
        JSONObject msgJson = new JSONObject(msgJsonStr);
         if( msgJson.has( "startTimer" ) ) {
             int tout = Integer.parseInt( msgJson.getString("startTimer") );
            try { Thread.sleep(tout); }catch( Exception e ){}
            System.out.println( myname + " | elapsed " + tout + " msecs killed=" + killed);
            if( owner != null && !killed ) owner.send(ActorMsgs.endTimerMsg);
        }
    }

    public void kill(){
        //System.out.println( myname + " | kill " );
        this.terminate();
        killed = true;
    }
}

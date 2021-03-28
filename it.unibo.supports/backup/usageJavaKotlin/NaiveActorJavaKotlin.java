package it.unibo.supports2021.usageJavaKotlin;

import it.unibo.actor0.ApplMessage;
import it.unibo.actor0.sysUtil;
import org.jetbrains.annotations.NotNull;

public class NaiveActorJavaKotlin extends ActorBasicJavaKotlin {

    public NaiveActorJavaKotlin(@NotNull String name) {
        super(name);
    }

    //protected void handleInput(String info){ showMsg("STRANGE:"+info); }

    @Override
    protected void handleInput( ApplMessage msg ) {
        if(  msg.getMsgId().equals("end") ){
            System.out.println("$name | ENDS"); terminate(); return; }
        else {
            //if (msg.getMsgId().equals("start")) return;
            showMsg(msg.toString() + " " + sysUtil.aboutThreads(this.getName()));
        }
    }
}

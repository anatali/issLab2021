package demo;

import it.unibo.actor0.ApplMessage;
import it.unibo.interaction.IUniboActor;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.SendChannel;
import org.jetbrains.annotations.NotNull;

public class NaiveActorObserverKJava extends AbstractActorKJava{
protected IUniboActor b;
    public NaiveActorObserverKJava(@NotNull String name, @NotNull CoroutineScope scope ) {
        super(name, scope );
    }



    @Override
    protected void handleMsg(ApplMessage msg, SendChannel<ApplMessage> myactor) {
        println("NaiveActorObserverKJava | handleInput" + msg );
         /*
        if( msg.getMsgContent().contains("stepdone")){
            ApplMessage m  = MsgUtil.buildDispatch("main", "robotmove", "turnLeft", "stepRobot");
            b.send( m.toString() );
        }

         */
    }
}

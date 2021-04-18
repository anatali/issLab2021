package demo;

import it.unibo.actor0.ActorBasicKotlin;
import it.unibo.actor0.ApplMessage;
import it.unibo.actor0.DispatchType;
import it.unibo.actor0.MsgUtil;
import it.unibo.interaction.IJavaActor;
import it.unibo.robotService.ApplMsgs;
import it.unibo.robotService.BasicStepRobotActor;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.SendChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NaiveActorObserverKJava extends AbstractActorKJava{
protected IJavaActor b;
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

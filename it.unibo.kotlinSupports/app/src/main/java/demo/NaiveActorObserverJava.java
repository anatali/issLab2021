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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NaiveActorObserverJava extends ActorBasicKotlin{
protected IJavaActor b;
    public NaiveActorObserverJava(@NotNull String name,
                                  @NotNull CoroutineScope scope,
                                  @NotNull DispatchType dispatchType,
                                  int channelSize ) {
        super(name, scope, dispatchType, channelSize);
        //this.owner =
        //b = new BasicStepRobotActor("stepRobot", this, this.getScope(), "localhost");
    }

    @Nullable
    @Override
    protected Object handleInput(@NotNull ApplMessage msg, @NotNull Continuation<? super Unit> $completion) {
        System.out.println("NaiveActorObserverJava | handleInput" + msg );
        /*
        if( msg.getMsgContent().contains("stepdone")){
            ApplMessage m  = MsgUtil.buildDispatch("main", "robotmove", "turnLeft", "stepRobot");
            b.send( m.toString() );
        }

         */
          return null;
    }
}

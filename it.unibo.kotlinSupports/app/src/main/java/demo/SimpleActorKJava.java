package demo;

import it.unibo.actor0.ActorBasicKotlin;
import it.unibo.actor0.ApplMessage;
import it.unibo.actor0.DispatchType;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.SendChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleActorKJava extends AbstractActorKJava {

    public SimpleActorKJava( @NotNull String name, @NotNull CoroutineScope scope ) {
        super( name, scope );
    }

    @Override
    protected void handleMsg(ApplMessage msg, SendChannel<ApplMessage> myactor) {
        println("SimpleActorKJava | handleMsg" + msg );
    }


}

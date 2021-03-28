package it.unibo.supports2021;

import it.unibo.actor0.ActorBasicKotlin;
import it.unibo.actor0.ApplMessage;
import it.unibo.actor0.DispatchType;
import it.unibo.actor0.MsgUtil;
import it.unibo.interaction.IJavaKotlinActor;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.GlobalScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ActorBasicJavaKotlin  extends ActorBasicKotlin implements IJavaKotlinActor {

    public ActorBasicJavaKotlin(@NotNull String name ) {
        super(name,  DispatchType.single,50);
    }

    @NotNull
    @Override
    public String myname() {
        return this.getName();
    }

    @Override
    public void send(@NotNull String msg) {
        ApplMessage m = MsgUtil.buildDispatch( myname(), "cmd" , msg, this.myname() );
        sendToYourself( m );
    }
}

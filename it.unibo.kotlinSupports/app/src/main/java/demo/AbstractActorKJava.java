package demo;

import com.andreapivetta.kolor.Color;
import com.andreapivetta.kolor.Kolor;
import it.unibo.actor0.ActorBasicKotlin;
import it.unibo.actor0.ApplMessage;
import it.unibo.actor0.DispatchType;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.channels.SendChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractActorKJava extends ActorBasicKotlin {

    public AbstractActorKJava(@NotNull String name, @NotNull CoroutineScope scope ) {
        super(name, scope, DispatchType.single, 30);
        colorPrint("AbstractActorKJava CREATED | " + name, Color.CYAN );
    }
    public AbstractActorKJava(@NotNull String name, @NotNull CoroutineScope scope, @NotNull DispatchType dispatchType ) {
        super(name, scope, dispatchType, 30);
        colorPrint("AbstractActorKJava CREATED | " + name, Color.CYAN );
    }

/*
Hides $completion details to application actors
Provides updating of the observers
 */
    @Nullable
    @Override
    protected Object handleInput(@NotNull ApplMessage msg, @NotNull Continuation<? super Unit> $completion) {
        colorPrint( getName() + " handleInput | " + msg, Color.CYAN );
        if( msg.getMsgId().equals("endKJava")) terminate();
        else{
            this.updateObservers(msg, $completion);
            handleMsg( msg, this.getKactor());
        }
        return null;
    }

    abstract protected void handleMsg(ApplMessage msg, SendChannel<ApplMessage> myactor) ;

    protected void println( String msg ){
        colorPrintNoTab(  msg, Color.BLACK  );
    }

}

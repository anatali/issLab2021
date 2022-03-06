package it.unibo.kactor;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

import static kotlinx.coroutines.ThreadPoolDispatcherKt.newSingleThreadContext;

public abstract class Actor22 extends ActorBasic {
    private static HashMap<String,ActorBasic> ctxMap = new HashMap<String,ActorBasic>();

    public static void addActor(ActorBasic a) {
        ctxMap.put(a.getName(), a);
        System.out.println("REGISTERD actor with name " + a.getName()  );

    }
    public static ActorBasic getActor(String actorName) {
        return ctxMap.get(actorName);
    }

    private static CoroutineScope createScope(){
        ExecutorCoroutineDispatcher d = newSingleThreadContext("single");
        CoroutineScope scope = CoroutineScopeKt.MainScope();
        return scope;
    }

    public static void sendMsg(String msgId,  String msg, ActorBasic destActor ){
        //null for kotlin.coroutines.Continuation<? super Unit> $completion
        destActor.forward(msgId,    msg,   destActor.getName(),null);
    }
    public static void sendMsg(IApplMessage msg, ActorBasic destActor ){
        //System.out.println("Actor22 | sendMsg " + msg + " dest=" + destActor.getName());
        destActor.autoMsg(msg,null);
    }
//private static CoroutineScope scope = createScope();


    public Actor22(@NotNull String name ) {
        super(name, createScope(), false, true, false, 50);
        if( getActor(name) == null ) addActor( this );
        else System.out.println("WARNING: an actor with name " + name + " already exixts");
    }

    @Nullable
    @Override
    public Object actorBody(@NotNull IApplMessage msg, @NotNull Continuation<? super Unit> $completion) {
        doJob(msg);
        return null;
    }



    protected abstract void doJob(IApplMessage msg);
}

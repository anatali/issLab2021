package it.unibo.kactor;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;



/*
Kotlin coroutines are implemented with a compiler transformation to the code
Java cannot use Kotlin's coroutines mechanic since it is a compile-time feature
 */

public abstract class Actor22 extends ActorBasic {
    private static HashMap<String,ActorBasic> ctxMap = new HashMap<String,ActorBasic>();
    protected kotlin.coroutines.Continuation<? super Unit> mycompletion;

    public static void createActorSystem(String name, String hostAddr, int port, String sysDescrFilename ){
        //runBlocking{
        //QakContext qkaCtx = new QakContext(name,   hostAddr,   port, "", false, false);
            //QakContext.Companion.createContexts( hostAddr,"localhost", null, sysDescrFilename, "sysRules.pl");
        //}
            QakContext.Companion.createContexts(hostAddr, QakContext.Companion.createScope(), sysDescrFilename, "sysRules.pl");
            //QakContextServer ctxserver = QakContextServer( this, GlobalScope.INSTANCE, name, Protocol.TCP );
            //sysUtil.INSTANCE.createContexts(hostAddr, sysDescrFilename, "sysRules.pl");
            //sysUtil.INSTANCE.traceprintln("%%% QakContext | CREATING NO ACTORS on $hostName ip=${ip.toString()}");

    }

    public static void addActor(ActorBasic a) {
        ctxMap.put(a.getName(), a);
        System.out.println("REGISTERD actor with name " + a.getName()  );

    }
    public static ActorBasic getActor(String actorName) {
        return ctxMap.get(actorName);
    }

    public static void showActors22(){
        System.out.println("-------------------------------------------------------");
        ctxMap.forEach((k,v) -> { System.out.println("name: "+k+" also in Qak:"+ QakContext.Companion.getActor(k) ); } );
        System.out.println("-------------------------------------------------------");
    }

    public static void sendAMsg(String msgId,  String msg, ActorBasic destActor ){
        //null for kotlin.coroutines.Continuation<? super Unit> $completion
        if( destActor instanceof Actor22 ){
            destActor.forward(msgId,    msg,   destActor.getName(),((Actor22) destActor).mycompletion);
        }
        else destActor.forward(msgId,    msg,   destActor.getName(),null);
    }
    public static void sendAMsg(IApplMessage msg, ActorBasic destActor ){
        if( destActor instanceof Actor22 ){
            destActor.autoMsg(msg,((Actor22) destActor).mycompletion);
        }
        //System.out.println("Actor22 | sendMsg " + msg + " dest=" + destActor.getName());
        else destActor.autoMsg(msg,null);
    }

    public static void sendReply(IApplMessage msg, IApplMessage reply) {
        System.out.println(   "Actor22 sendReply | reply= " + reply );
        ActorBasic dest = getActor(msg.msgSender());
        if(dest != null) sendAMsg(reply, dest);
        else {
            ActorBasic ar = getActor("ar"+msg.msgSender());
            if(ar !=null) sendAMsg(reply, ar);
            else {
                System.out.println("Actor22 sendReply | Reply IMPOSSIBLE");
             }
        }
    }


    public Actor22(@NotNull String name ) {
        super(name, QakContext.Companion.createScope(), false, true, false, 50);
        if( getActor(name) == null ) addActor( this );
        else System.out.println("WARNING: an actor with name " + name + " already exists");
    }

    @Nullable
    @Override
    public Object actorBody(@NotNull IApplMessage msg, @NotNull Continuation<? super Unit> $completion) {
        mycompletion = $completion;
        //System.out.println(getName()+ " $$$ mycompletion= "+ mycompletion + " msg=" + msg);
        doJob(msg);
        return null;
    }
    public void forwardToSelf( IApplMessage msg ){
        this.autoMsg( msg, mycompletion);
    }
    public void forwardToSelf(String msgId,  String msg ){
        this.autoMsg(msgId, msg, mycompletion);
    }
    public void forward(String msgId,  String msg, String dest ){
        this.forward(msgId, msg, dest, mycompletion);
    }
    public void request( String msgId,  String msg, String dest ){
        this.request(msgId, msg, dest, mycompletion);
    }
    public void autoMsg( IApplMessage msg ){
        this.autoMsg(msg, mycompletion);
    }

    public void sendMsg(IApplMessage msg, ActorBasic destActor ){
        //System.out.println("Actor22 | sendMsg " + msg + " dest=" + destActor.getName());
        //destActor.autoMsg( msg, mycompletion);
        destActor.forward( msg.msgId(), msg.msgContent(), destActor.getName(), mycompletion);
    }
    public void sendMsg(String msgId,  String msg, ActorBasic destActor ){
        destActor.forward(msgId, msg, destActor.getName(),mycompletion);
    }

    protected void sendAnswer(IApplMessage msg, IApplMessage reply) {
        System.out.println( getName()  + " | reply= " + reply );
        ActorBasic dest = getActor(msg.msgSender());
        if(dest != null) sendMsg(reply, dest);
        else {
            ActorBasic ar = getActor("ar"+msg.msgSender());
            if(ar !=null) sendMsg(reply, ar);
            else {
                System.out.println(getName() + " | Reply IMPOSSIBLE");
                //forward(reply.msgId(), reply.msgContent(), reply.msgReceiver());
                //this.answer(msg.msgId(),reply.msgId(),reply.toString(),null);
            }
        }
    }

    protected abstract void doJob(IApplMessage msg);
}

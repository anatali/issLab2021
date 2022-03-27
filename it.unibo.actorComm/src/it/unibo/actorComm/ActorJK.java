package it.unibo.actorComm;


import it.unibo.actorComm.annotations.AnnotUtil;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.*;
import java.util.HashMap;

public  class ActorJK   {
    private static HashMap<String,ProxyAsClient> proxyMap = new HashMap<String,ProxyAsClient>();

    public static void createLocalActors(Object element) {
    	AnnotUtil.createActorLocal(element);
    }
    public static void createRemoteActors(Object element) {
     	AnnotUtil.createActorRemote(element);
    }

    public static void handleActorDeclaration(Object element) {
    	AnnotUtil.createActorLocal(element);
    	AnnotUtil.createActorRemote(element);
    }
    
    public static void setActorAsRemote(String actorName, String entry, String host, ProtocolType protocol ) {
    	if( ! proxyMap.containsKey(actorName)   ) { //defensive
    		ProxyAsClient pxy = new ProxyAsClient(actorName+"Pxy", host, entry, protocol);
    		proxyMap.put(actorName, pxy);
    	}   	
    }
    


    public static void showActors22(){
    	Actor22.showActors22();
    }

    public static void sendAMsg(String msgId,  String msg, ActorBasic destActor ){
        //null for kotlin.coroutines.Continuation<? super Unit> $completion
        if( destActor instanceof Actor22 ){
            destActor.forward(msgId,    msg,   destActor.getName(),((Actor22) destActor).getmycompletion());
        }
        else destActor.forward(msgId,    msg,   destActor.getName(),null);
    }


    public static void sendAMsg(IApplMessage msg, ActorBasic destActor ){
        if( destActor instanceof Actor22 ){
            destActor.autoMsg(msg, ((Actor22) destActor).getmycompletion());
        }
        //System.out.println("Actor22 | sendMsg " + msg + " dest=" + destActor.getName());
        else destActor.autoMsg(msg,null);
    }
    
    protected static void autoMsg(Actor22 a, IApplMessage msg) {
		try {
			a.autoMsg(msg,  a.getmycompletion() );
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    public static void sendAMsg( IApplMessage msg ){
    	sendAMsg( msg, msg.msgReceiver() );
    }
    
    public static void sendAMsg(IApplMessage msg, String destActorName){
        Actor22 a = (Actor22) Actor22.getActor(destActorName);
		ColorsOut.out("sendAMsg to" + a , ColorsOut.MAGENTA);
        if( a != null ) {
        	autoMsg(a,msg);
        }else{ //invio di un msg ad un attore non locale : cerco in proxyMap
			ProxyAsClient pxy = proxyMap.get(destActorName);
			if( pxy != null ) {
				if( msg.isRequest() ) {
					String answerMsg  = pxy.sendRequestOnConnection( msg.toString()) ;
					IApplMessage reply= new ApplMessage( answerMsg );
					ColorsOut.out("answer=" + reply.msgContent() , ColorsOut.MAGENTA);
					Actor22 sender = Actor22.getActor(msg.msgSender());
					autoMsg( sender,reply); //WARNING: the sender must handle the reply as msg
				}else {
					pxy.sendCommandOnConnection(msg.toString());
				}
			}
        }
      }

    public static void sendReply(IApplMessage msg, IApplMessage reply) {
        //System.out.println(   "Actor22 sendReply | reply= " + reply );
        Actor22 dest = Actor22.getActor(msg.msgSender());
        if(dest != null) sendAMsg(reply, dest);
        else {
        	Actor22 ar = Actor22.getActor("ar"+msg.msgSender());
            if(ar !=null) sendAMsg(reply, ar);
            else {
                System.out.println("Actor22 sendReply | Reply IMPOSSIBLE");
             }
        }
    }

/*
    public ActorJK(@NotNull String name ) {
        super(name, QakContext.Companion.createScope(), false, true, false, 50);
        if( getActor(name) == null ) addActor( this );
        else System.out.println("Actor22 WARNING: an actor with name " + name + " already exists");
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
    */
}

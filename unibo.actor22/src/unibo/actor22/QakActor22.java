package unibo.actor22;

import java.util.HashMap;

import org.jetbrains.annotations.NotNull;

import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.*;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

public abstract class QakActor22 extends ActorBasic{

protected kotlin.coroutines.Continuation<? super Unit> mycompletion;

	public QakActor22(@NotNull String name ) {      
		super(name, QakContext.Companion.createScope(), false, true, false, 50);
        if( Qak22Context.getActor(name) == null ) {
        	Qak22Context.addActor( this );
        	ColorsOut.outappl( getName()  + " | CREATED " , ColorsOut.CYAN);
        }
        else ColorsOut.outerr("QakActor22 | WARNING: an actor with name " + name + " already exists");	
	}


    
	protected abstract void handleMsg(IApplMessage msg);
	
	@Override
	public Object actorBody(@NotNull IApplMessage msg, @NotNull Continuation<? super Unit> $completion) {
        mycompletion = $completion;
        handleMsg(msg);
        return null;
	}
	
	//Invia la richiesta di elaborazione di un messaggio all'attore
	protected void queueMsg(IApplMessage msg) {
		super.autoMsg(msg, mycompletion);
	}
	
/*
 * INVIO MESSAGGI	 
 */
//	protected void sendMessageToActor(IApplMessage msg, String destName ) {
//			//,IConnInteraction conn, @NotNull  Continuation<? super Unit> $completion ) {
//		ColorsOut.out("QakActor22 | sendMessageToActor " + msg + " to:" + destName , ColorsOut.CYAN);
//		super.sendMessageToActor( msg, destName, null, mycompletion); //null is IConnInteraction
//	}
	
	protected void sendMsg( IApplMessage msg ){
		sendMsg(msg,msg.msgReceiver());
	}
	
    protected void sendMsg(IApplMessage msg, String destActorName){
		//ColorsOut.out("Qak22Util | sendAMsg " + msg  , ColorsOut.GREEN);	  
        QakActor22 dest = Qak22Context.getActor(destActorName);  
        if( dest != null ) { //attore locale
    		ColorsOut.out("QakActor22 | sendAMsg " + msg + " to:" + dest.getName() , ColorsOut.GREEN);
    		dest.queueMsg(msg);
        }else{ //invio di un msg ad un attore non locale : cerco in proxyMap
//			ProxyAsClient pxy = proxyMap.get(destActorName);
//    		ColorsOut.out("QakActor22 | sendAMsg " + msg + " using:" + pxy , ColorsOut.GREEN);
//			if( pxy != null ) {
//				if( msg.isRequest() ) {
//					String answerMsg  = pxy.sendRequestOnConnection( msg.toString()) ;
//					IApplMessage reply= new ApplMessage( answerMsg );
//					ColorsOut.out("QakActor22 | answer=" + reply.msgContent() , ColorsOut.GREEN);
//					QakActor22 sender = Qak22Context.getActor(msg.msgSender());
//					sender.elabMsg(reply); //WARNING: the sender must handle the reply as msg
//				}else {
//					pxy.sendCommandOnConnection(msg.toString());
//				}
//			}
        }
	}
	
	
    public void autoMsg( IApplMessage msg ){
    	//WARNING: il sender di msg potrebbe essere qualsiasi
     	if( msg.msgReceiver().equals( getName() )) sendMsg(msg,msg.msgReceiver());
    		//sendMessageToActor(msg,msg.msgReceiver()); 
    	else ColorsOut.outerr("QakActor22 | autoMsg wrong receiver");
    }
	public void forward( IApplMessage msg ){
		ColorsOut.outappl( "QakActor22 forward:" + msg, ColorsOut.CYAN);
		if( msg.isDispatch() ) sendMsg( msg ); //sendMessageToActor(msg, msg.msgReceiver());
		else ColorsOut.outerr("QakActor22 | forward requires a dispatch");
	}
 
    public void request( IApplMessage msg ){
    	if( msg.isRequest() ) sendMsg( msg );//sendMessageToActor(msg, msg.msgReceiver());
    	else ColorsOut.outerr("QakActor22 | forward requires a request");
    }
    
    
	protected void sendReply(IApplMessage msg, IApplMessage reply) {
		QakActor22 dest = Qak22Context.getActor( msg.msgSender() );
        if(dest != null) dest.queueMsg( reply );
        else { //ci potrebbe essere un attore costruito ad hoc per ricevere la risposta
        	QakActor22 ar = Qak22Context.getActor("ar"+msg.msgSender());  
            if(ar !=null) ar.queueMsg( reply );
            else ColorsOut.outerr("QakActor22 | WARNING: reply IMPOSSIBLE");
        }
    }	
	
}

package unibo.actor22;

import org.jetbrains.annotations.NotNull;
import it.unibo.kactor.*;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import unibo.actor22comm.proxy.ProxyAsClient;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;

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
 
	
	protected void sendMsg( IApplMessage msg ){
     	String destActorName=msg.msgReceiver();
		//ColorsOut.out("Qak22Util | sendAMsg " + msg  , ColorsOut.GREEN);	  
        QakActor22 dest = Qak22Context.getActor(destActorName);  
        if( dest != null ) { //attore locale
    		ColorsOut.out("QakActor22 | sendAMsg " + msg + " to:" + dest.getName() , ColorsOut.GREEN);
    		dest.queueMsg(msg);
        }else{  
        	sendMsgToRemoteActor(msg);
         }
	}
	
	protected void sendMsgToRemoteActor( IApplMessage msg ) {
		String destActorName = msg.msgReceiver();
		//Occorre un proxy al contesto
		ProxyAsClient pxy    = Qak22Context.getProxy(destActorName);
		//ColorsOut.out("QakActor22 | sendAMsg " + msg + " using:" + pxy , ColorsOut.GREEN);
		if( pxy == null ) {
			ColorsOut.outerr("Perhaps no setActorAsRemote for " + destActorName );
			return;
		}
 		if( msg.isRequest() ) { doRequest(msg,pxy);
		}else { pxy.sendCommandOnConnection(msg.toString());
		}	
	}
	
	protected void doRequest(IApplMessage msg,  ProxyAsClient pxy ) {
		//CommUtils.aboutThreads("QakActor22 Before doRequest - ");
  		new Thread() {
			public void run() {
		 		//ColorsOut.out( "QakActor22  | doRequest " + msg + " pxy=" + pxy, ColorsOut.WHITE_BACKGROUND  );
				String answerMsg  = pxy.sendRequestOnConnection( msg.toString()) ;
				//Attende la risposta  
				IApplMessage reply= new ApplMessage( answerMsg );
				//ColorsOut.outappl("QakActor22 | answer=" + reply  , ColorsOut.WHITE_BACKGROUND);
				QakActor22 sender = Qak22Context.getActor(msg.msgSender());
				if( sender != null ) //defensive
					sender.queueMsg(reply); //the sender must handle the reply as msg	
				else ColorsOut.outerr("QakActor22 | answer " + answerMsg + " for an unknown actor " + msg.msgSender());
			}			
		}.start();
		CommUtils.aboutThreads("QakActor22 After doRequest - ");
	}
	
	
	protected void autoMsg( IApplMessage msg ){
    	//WARNING: il sender di msg potrebbe essere qualsiasi
     	if( msg.msgReceiver().equals( getName() )) sendMsg( msg );
    		//sendMessageToActor(msg,msg.msgReceiver()); 
    	else ColorsOut.outerr("QakActor22 | autoMsg wrong receiver");
    }
    
	protected  void forward( IApplMessage msg ){
		ColorsOut.outappl( "QakActor22 forward:" + msg, ColorsOut.CYAN);
		if( msg.isDispatch() ) sendMsg( msg ); //sendMessageToActor(msg, msg.msgReceiver());
		else ColorsOut.outerr("QakActor22 | forward requires a dispatch");
	}
 
	protected void request( IApplMessage msg ){
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

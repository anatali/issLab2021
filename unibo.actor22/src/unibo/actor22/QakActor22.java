package unibo.actor22;

import java.util.HashMap;

import org.jetbrains.annotations.NotNull;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.is.interfaces.protocols.IConnInteraction;
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


    
	protected abstract void doJob(IApplMessage msg);
	
	@Override
	public Object actorBody(@NotNull IApplMessage msg, @NotNull Continuation<? super Unit> $completion) {
        mycompletion = $completion;
        //System.out.println(getName()+ " $$$ mycompletion= "+ mycompletion + " msg=" + msg);
        doJob(msg);
        return null;
	}
	
	//Invia la richiesta di elaborazione di un messaggio all'attore
	public void elabMsg(IApplMessage msg) {
		super.autoMsg(msg, mycompletion);
	}
	
	 
	protected void sendMessageToActor(IApplMessage msg, String destName ) {
			//,IConnInteraction conn, @NotNull  Continuation<? super Unit> $completion ) {
		super.sendMessageToActor( msg, destName, null, mycompletion); //null is IConnInteraction
	}
	
	protected void sendReply(IApplMessage msg, IApplMessage reply) {
		QakActor22 dest = Qak22Context.getActor( msg.msgSender() );
        if(dest != null) dest.elabMsg( reply );
        else { //ci potrebbe essere un attore costruito ad hoc per ricevere la risposta
        	QakActor22 ar = Qak22Context.getActor("ar"+msg.msgSender());  
            if(ar !=null) dest.elabMsg( reply );
            else {
                ColorsOut.outerr("QakActor22 | WARNING: reply IMPOSSIBLE");
             }
        }
    }	
	
}

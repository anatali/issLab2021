package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.ApplMessage;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.GlobalScope;

public class CounterActorWithDelay extends ActorBasic{
	private int n = 2;
	
	public CounterActorWithDelay( String name  ) {
		super(name, GlobalScope.INSTANCE, false,false, false,50);
	}

	@Override
	public Object actorBody(ApplMessage msg, Continuation<? super Unit> arg1) {
 		ColorsOut.outappl( msg.toString(), ColorsOut.MAGENTA );
 		if( msg.msgId().equals("inc")) { n = n + 1; }
 		else if( msg.msgId().equals("dec")) {
 			int dt = Integer.parseInt( msg.msgContent() );
 			int v = n;
 		    v = v - 1;
 		    Utils.delay(dt);  //the control is given to another client
 		    ColorsOut.outappl(getName() + " | resumes v= " + v, ColorsOut.MAGENTA);
 		    n = v;
 		    ColorsOut.outappl(getName() + " | new value after dec= " + n, ColorsOut.MAGENTA);
 		}
		return null;
	}

}

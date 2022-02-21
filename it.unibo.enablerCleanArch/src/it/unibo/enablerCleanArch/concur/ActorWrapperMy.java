package it.unibo.enablerCleanArch.concur;
 

import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.ApplMessage;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.GlobalScope;

public abstract class ActorWrapperMy extends ActorBasic{
	
	public ActorWrapperMy( String name  ) {
		super(name, GlobalScope.INSTANCE, false,false, false,50);
	}
	@Override
	public Object actorBody(ApplMessage msg, Continuation<? super Unit> arg1) {
		doJob(msg);
		return null;
	}
	
	protected abstract void doJob(ApplMessage msg);

}

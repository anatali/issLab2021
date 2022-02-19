package it.unibo.qaktest;

import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.ActorBasicFsm;
import it.unibo.kactor.ApplMessage;
import kotlin.*;
import kotlin.coroutines.*;

public class QakTest extends ActorBasic {
	
	public QakTest(String name) {
		super( name, null, false,false, false,50);
	}

	@Override
	public Object actorBody(ApplMessage arg0, Continuation<? super Unit> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}

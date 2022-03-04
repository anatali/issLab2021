package it.unibo.radarSystem22.actors.domain;

import it.unibo.kactor.ActorWrapper;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.interfaces.ILed;

/*
 * Funge da interprete di 
 */
public class LedMockActor extends ActorWrapper{
private ILed led;

	public LedMockActor(String name) {
		super(name);
	}

	@Override
	protected void doJob(IApplMessage arg0) {
		 
		
	}

 

}

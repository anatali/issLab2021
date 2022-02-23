package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.kactor.ActorWrapper;
import it.unibo.kactor.ApplMessage;

public class CounterActorWithDelay extends ActorWrapper{ 
	private int n = 2;
	
	public CounterActorWithDelay( String name  ) {
		super( name );
	}

	@Override
	public void doJob( ApplMessage msg ) {
 		//ColorsOut.outappl( arg1.toString(), ColorsOut.BLACK );
		ColorsOut.outappl( getName() + " | " + msg.toString(), ColorsOut.MAGENTA );
 		if( msg.msgId().equals("inc")) { n = n + 1; }
 		else if( msg.msgId().equals("dec")) {
 			int dt = Integer.parseInt( msg.msgContent() );
 			int v = n;
 		    v = v - 1;
 		    Utils.delay(dt);  //the control is given to another client
 		    ColorsOut.outappl(getName() + " | resumes v= " + v, ColorsOut.MAGENTA);
 		    n = v;
 		    ColorsOut.outappl(getName() + " | new value after dec= " + n, ColorsOut.MAGENTA);
 	 		Utils.aboutThreads(getName() + " | CounterWithDelay after dec - ");
 		}
 	}

}

package it.unibo.radarSystem22.actors.counter;

 
import it.unibo.kactor.ActorWrapper;
import it.unibo.kactor.ApplMessage;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;

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
 		    BasicUtils.delay(dt);  //the control is given to another client
 		    ColorsOut.outappl(getName() + " | resumes v= " + v, ColorsOut.MAGENTA);
 		    n = v;
 		    ColorsOut.outappl(getName() + " | new value after dec= " + n, ColorsOut.MAGENTA);
 		   BasicUtils.aboutThreads(getName() + " | CounterWithDelay after dec - ");
 		}
 	}

}

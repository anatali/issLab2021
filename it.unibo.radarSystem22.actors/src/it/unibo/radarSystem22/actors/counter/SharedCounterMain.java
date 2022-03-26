package it.unibo.radarSystem22.actors.counter;
import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.context.EnablerContextForActors;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;

 

 
public class SharedCounterMain  {
public static final int ctxServerPort   = 7070;

/*
 * La business logic è definita in  
 * CounterActor, che include il CounterWithDelay.
 */
	public void configure(  ) {
 		BasicUtils.aboutThreads("SharedCounterWithActorsMain | Before configure - ");
		CommSystemConfig.tracing     = true;
		EnablerContextForActors contextServer = new EnablerContextForActors("ctxEnblr", ctxServerPort, ProtocolType.tcp );
  		new CounterActor("counter");
		contextServer.activate();    
 	}
 	 
  
	public static void main( String[] args)   {	
		CommSystemConfig.tracing=false;
 		SharedCounterMain sys = new SharedCounterMain();
 		sys.configure();
 		//BasicUtils.delay(2000);
  		BasicUtils.aboutThreads("Before end - ");
	}
}

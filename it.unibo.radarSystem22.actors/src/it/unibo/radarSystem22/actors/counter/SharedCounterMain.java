package it.unibo.radarSystem22.actors.counter;
import it.unibo.actorComm.context.TcpContextServer;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;

 

 
public class SharedCounterMain  {
public static final int ctxServerPort   = 7070;

/*
 * La business logic è definita in un attore  CounterWithDelay 
 * incapsulato in CounterActor incluso in CounterMsgHandler
 * che viene aggiunto al TcpContextServer che fornisce la capacità di interagire in rete
 */
	public void configure(  ) {
 		BasicUtils.aboutThreads("SharedCounterWithActorsMain | Before configure - ");
		TcpContextServer contextServer   = new TcpContextServer("TcpContextServer",  ctxServerPort );
		//CounterActor counterH   = new CounterActor( "applActorH" );
		//CounterActor ca = new CounterActor("ca");
		CounterMsgHandler ch = new CounterMsgHandler("counterHandler");
 		contextServer.addComponent("counter",ch);	
 		contextServer.activate();    
 	}
 	 
  
	public static void main( String[] args)   {	
		CommSystemConfig.tracing=false;
 		SharedCounterMain sys = new SharedCounterMain();
 		sys.configure();
  		BasicUtils.aboutThreads("Before end - ");
	}
}

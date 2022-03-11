package it.unibo.radarSystem22.actors.counter;
import it.unibo.actorComm.context.TcpContextServer;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;

 

 
public class SharedCounterMain  {
public static final int ctxServerPort   = 7070;

/*
 * La business logic è definita in CounterMsgHandler che incapsula un attore   
 * CounterActor, che include il CounterWithDelay.
 * CounterMsgHandler viene aggiunto al TcpContextServer che fornisce la 
 * capacità di interagire in rete.
 */
	public void configure(  ) {
 		BasicUtils.aboutThreads("SharedCounterWithActorsMain | Before configure - ");
		TcpContextServer contextServer = new TcpContextServer("TcpContextServer",  ctxServerPort );
		CounterApplHandler ch           = new CounterApplHandler("counterHandler");
 		contextServer.addComponent("counter",ch);	
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

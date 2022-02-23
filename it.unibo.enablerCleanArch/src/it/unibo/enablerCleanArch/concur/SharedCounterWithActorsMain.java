package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.context.TcpContextServer;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.ActorWrapper;
import it.unibo.kactor.MsgUtil;


/*
 * Un ATTORE contatore di nome 'counter' (classe CounterActorWithDelay) con valore iniziale 2 
 * esegue l'operazione dec rilasciando il controllo per un certo tempo.
 * Questo contatore viene reso capace di gestire messaggi da un CounterApplHandler che lo incapsula.
 * Due client inviano il comando (dispatch) dec a 'counter', che VA A 0
 * Il sistema attiva 5 thread 
 * (main, TcpContextSerer, due client e UN SOLO Thread per tutti gli attori )
 */
 
public class SharedCounterWithActorsMain  {
private int ctxServerPort   = 7070;
private String delay        = "1000"; //con delay = 0 funziona

ApplMessage msgDec = new ApplMessage(
	      "msg( cmd, dispatch, main, counter, dec(DELAY), 1 )"
	      .replace("DELAY", delay));
 
	public void configure(  ) {
 		Utils.aboutThreads("Before configure - ");
 		CounterActorWithDelay counter    = new CounterActorWithDelay("counter");
		TcpContextServer contextServer   = new TcpContextServer("TcpContextServer",  ctxServerPort );
		CounterApplHandler counterH      = new CounterApplHandler("counterH", counter);
		contextServer.addComponent(counter.getName(),counterH);	
		contextServer.activate();    
 	}
	
	public void execute() throws Exception {
		ProxyAsClient client1 = new ProxyAsClient("client1","localhost", ""+ctxServerPort, ProtocolType.tcp);
 		ProxyAsClient client2 = new ProxyAsClient("client2","localhost", ""+ctxServerPort, ProtocolType.tcp);
 		Utils.aboutThreads("After client creation - ");
 		client1.sendCommandOnConnection(msgDec.toString());  
 		client2.sendCommandOnConnection(msgDec.toString());		
 		workWithOtherCounterActors();
	}
	
	protected void workWithOtherCounterActors() {
		//Attivo altri attori counter
		ActorWrapper.Companion.setTrace();
		ColorsOut.outappl("---------------------------------------------------", ColorsOut.CYAN);
		for( int i=1; i<=5; i++) {
			String actorName = "ca"+i;
			ActorBasic a = new CounterActorWithDelay( actorName );
			it.unibo.kactor.ApplMessage msgDec = 
					MsgUtil.buildDispatch("main", "dec", "1000", actorName);
			MsgUtil.sendMsg(msgDec, a, null);
		}		
 	}
 
 
	public static void main( String[] args) throws Exception {	
		SharedCounterWithActorsMain sys = new SharedCounterWithActorsMain();
		RadarSystemConfig.withContext = true;
		RadarSystemConfig.tracing     = false;
		
		sys.configure();
		sys.execute();
// 		Thread.sleep(2500);
 		Utils.aboutThreads("Before end - ");
//		System.exit(0);
	}
}

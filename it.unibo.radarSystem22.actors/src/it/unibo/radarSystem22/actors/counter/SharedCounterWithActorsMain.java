package it.unibo.radarSystem22.actors.counter;

import it.unibo.comm2022.ProtocolType;
import it.unibo.comm2022.context.TcpContextServer;
import it.unibo.comm2022.proxy.ProxyAsClient;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.ActorWrapper;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;

 
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
 		BasicUtils.aboutThreads("Before configure - ");
 		CounterActorWithDelay counter    = new CounterActorWithDelay("counter");
		TcpContextServer contextServer   = new TcpContextServer("TcpContextServer",  ctxServerPort );
		CounterApplHandler counterH      = new CounterApplHandler("counterH", counter);
 		contextServer.addComponent("counter",counterH);	
 		contextServer.activate();    
 	}
	
	public void execute()  {
		ProxyAsClient client1 = new ProxyAsClient("client1","localhost", ""+ctxServerPort, ProtocolType.tcp);
 		ProxyAsClient client2 = new ProxyAsClient("client2","localhost", ""+ctxServerPort, ProtocolType.tcp);
 		BasicUtils.aboutThreads("After client creation - ");
 		client1.sendCommandOnConnection(msgDec.toString());  
 		client2.sendCommandOnConnection(msgDec.toString());		
 		//workWithOtherCounterActors();
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
 
 
	public static void main( String[] args)   {	
		ColorsOut.outappl("---------------------------------------------------", ColorsOut.CYAN);
 		SharedCounterWithActorsMain sys = new SharedCounterWithActorsMain();
 		sys.configure();
 		sys.execute();
 		BasicUtils.aboutThreads("Before end - ");
//		System.exit(0);
	}
}

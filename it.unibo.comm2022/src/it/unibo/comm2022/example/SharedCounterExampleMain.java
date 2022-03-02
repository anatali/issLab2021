package it.unibo.comm2022.example;

import it.unibo.comm2022.ApplMessage;
import it.unibo.comm2022.ProtocolType;
import it.unibo.comm2022.context.TcpContextServer;
import it.unibo.comm2022.interfaces.IApplMessage;
import it.unibo.comm2022.proxy.ProxyAsClient;
import it.unibo.comm2022.utils.BasicUtils;
import it.unibo.comm2022.utils.CommSystemConfig;


/*
 * Un oggetto contatore di nome 'counter' (classe CounterWithDelay) con valore iniziale 2 
 * esegue l'operazione dec rilasciando il controllo per un certo tempo.
 * Questo contatore viene reso capace di gestire messaggi da un CounterApplHandler che lo incapsula.
 * Due client inviano il comando (dispatch) dec a 'counter', che però non va a 0
 * Il sistema attiva 4 thread (main, TcpContextSerer e due client)
 */
public class SharedCounterExampleMain  {
private int ctxServerPort   = 7070;
private String delay        = "1000"; //con delay = 0 funziona

IApplMessage msgDec = new ApplMessage(
	      "msg( dec, dispatch, main, counter, dec(DELAY), 1 )"
	      .replace("DELAY", delay));
 
	public void configure(  ) {
 		BasicUtils.aboutThreads("Before configure - ");
		CounterWithDelay counter         = new CounterWithDelay("counter");
 		TcpContextServer contextServer   = new TcpContextServer("TcpContextServer",  ctxServerPort );
		CounterApplHandler counterH      = new CounterApplHandler("counterH", counter);
		contextServer.addComponent(counter.getName(),counterH);	
 		contextServer.activate();    
 		BasicUtils.aboutThreads("After configure - ");
 	}
	
	public void execute() throws Exception {
		ProxyAsClient client1 = new ProxyAsClient("client1","localhost", ""+ctxServerPort, ProtocolType.tcp);
 		ProxyAsClient client2 = new ProxyAsClient("client2","localhost", ""+ctxServerPort, ProtocolType.tcp);
 		client1.sendCommandOnConnection(msgDec.toString());  
 		client2.sendCommandOnConnection(msgDec.toString());		
 		BasicUtils.aboutThreads("After client send - ");
	}
 
 
	public static void main( String[] args) throws Exception {	
		SharedCounterExampleMain sys = new SharedCounterExampleMain();
		CommSystemConfig.withContext = true;
		CommSystemConfig.tracing     = false;
		
		sys.configure();
		sys.execute();
 		Thread.sleep(2500);
 		BasicUtils.aboutThreads("Before end - ");
		System.exit(0);
	}
}

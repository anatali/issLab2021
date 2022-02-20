package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.context.TcpContextServer;


public class SharedCounterExampleMain  {
private int ctxServerPort   = 7070;
private String delay        = "50"; //con delay = 0 funziona

ApplMessage msgDec = new ApplMessage(
	      "msg( cmd, dispatch, main, counter, dec(DELAY), 1 )"
	      .replace("DELAY", delay));
 
	public void configure(  ) {
		CounterWithDelay counter         = new CounterWithDelay("counter");
		TcpContextServer contextServer   = new TcpContextServer("TcpContextServer",  ctxServerPort );
		CounterApplHandler counterH      = new CounterApplHandler("counterH", counter);
		contextServer.addComponent(counter.getName(),counterH);	 
		contextServer.activate();   
 	}
	
	public void execute() throws Exception {
		ProxyAsClient client1 = new ProxyAsClient("client1","localhost", ""+ctxServerPort, ProtocolType.tcp);
 		ProxyAsClient client2 = new ProxyAsClient("client2","localhost", ""+ctxServerPort, ProtocolType.tcp);
		client1.sendCommandOnConnection(msgDec.toString());  
 		client2.sendCommandOnConnection(msgDec.toString());		
	}
 
 
	public static void main( String[] args) throws Exception {		
		SharedCounterExampleMain sys = new SharedCounterExampleMain();
		RadarSystemConfig.withContext = true;
		RadarSystemConfig.tracing     = false;
		sys.configure();
		sys.execute();
		Thread.sleep(3000);
		System.exit(0);
	}
}

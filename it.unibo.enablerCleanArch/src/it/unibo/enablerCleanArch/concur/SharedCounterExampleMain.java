package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.supports.TcpContextServer;
import it.unibo.enablerCleanArch.supports.TcpServer;
import it.unibo.enablerCleanArchapplHandlers.ContextMsgHandler;


public class SharedCounterExampleMain  {
private int ctxServerPort   = 7070;
private String resourceName = "counterHandler";
private String delay        = "200"; //con delay = 20 funziona

ApplMessage msgDec = new ApplMessage(
	      "msg( cmd, dispatch, main, DEST, dec(DELAY), 1 )"
	      .replace("DEST", resourceName).replace("DELAY", delay));
 
	public void configure(  ) {
		TcpContextServer contextServer   = new TcpContextServer("TcpContextServer",  ctxServerPort );
		CounterHandler counter           = new CounterHandler(resourceName);
		contextServer.addComponent(counter.getName(),counter);	
		contextServer.activate();   
 	}
	
	public void execute() throws Exception {
		ProxyAsClient client1 = new ProxyAsClient("client1","localhost", ""+ctxServerPort, ProtocolType.tcp);
 		ProxyAsClient client2 = new ProxyAsClient("client2","localhost", ""+ctxServerPort, ProtocolType.tcp);
		client1.sendCommandOnConnection(msgDec.toString()); //TODO: msg-related operators
 		client2.sendCommandOnConnection(msgDec.toString());		
	}
 
 
	public static void main( String[] args) throws Exception {		
		SharedCounterExampleMain sys = new SharedCounterExampleMain();
		sys.configure();
		sys.execute();
		Thread.sleep(1000);
	}
}

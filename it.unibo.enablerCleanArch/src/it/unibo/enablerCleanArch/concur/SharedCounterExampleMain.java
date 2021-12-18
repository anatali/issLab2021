package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.ContextMsgHandler;
import it.unibo.enablerCleanArch.supports.TcpServer;


public class SharedCounterExampleMain  {
private int ctxServerPort   = 7070;
private String resourceName = "counterHandler";
private String delay        = "20"; 

private ApplMessage msg1 = new ApplMessage(
		"msg( dec, dispatch, main, DEST, dec(DELAY), 1 )"
		.replace("DEST", resourceName).replace("DELAY", delay));
 
	public void configure(  ) {
		ContextMsgHandler ctxH    = new ContextMsgHandler("ctxH");
		TcpServer contextServer   = new TcpServer("TcpContextServer",  ctxServerPort, ctxH);
		CounterHandler counter    = new CounterHandler(resourceName);
		ctxH.addComponent(counter.getName(),counter);	
		contextServer.activate();   
 	}
	
	public void execute() throws Exception {
 		CounterClient client1 = new CounterClient("client1","localhost",ctxServerPort);
		CounterClient client2 = new CounterClient("client2","localhost",ctxServerPort);
		client1.sendCommandOnConnection(msg1.toString()); //TODO: msg-related operators
		client2.sendCommandOnConnection(msg1.toString());		
	}
 
 
	public static void main( String[] args) throws Exception {		
		SharedCounterExampleMain sys = new SharedCounterExampleMain();
		sys.configure();
		sys.execute();
		Thread.sleep(1000);
	}
}

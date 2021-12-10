package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.TcpContextServer;

public class SharedCounterExampleMain  {
private int serverPort      = 7171;
private String resourceName = "counterHandler";
private String delay        = "200";

private ApplMessage msg1 = new ApplMessage(
		"msg( dec, request, main, DEST, dec(DELAY), 1 )"
		.replace("DEST", resourceName).replace("DELAY", delay));
 
	public void configure(  ) {
		TcpContextServer contextServer  = new TcpContextServer("TcpContextServer", serverPort);
		CounterHandler counter          = new CounterHandler(resourceName);
		contextServer.addComponent(counter.getName(),counter);	
		contextServer.activate();   
 	}
	
	public void execute() throws Exception {
 		CounterClient client1 = new CounterClient("client1","localhost",serverPort);
		CounterClient client2 = new CounterClient("client2","localhost",serverPort);
		client1.sendValueOnConnection(msg1.toString()); //TODO: msg-related operators
		client2.sendValueOnConnection(msg1.toString());		
	}

 
	public static void main( String[] args) throws Exception {		
		SharedCounterExampleMain sys = new SharedCounterExampleMain();
		sys.configure();
		sys.execute();
		Thread.sleep(1000);
	}
}

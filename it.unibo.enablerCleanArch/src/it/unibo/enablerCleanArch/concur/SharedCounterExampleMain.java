package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.TcpContextServer;

public class SharedCounterExampleMain  {
private int serverPort = 7171;
private String resourceName = "counter";

private ApplMessage msg1 = new ApplMessage("msg( dec, dispatch, main, counter, dec(10), 1 )");
private ApplMessage msg2 = new ApplMessage("msg( dec, dispatch, main, counter, dec(0), 1 )");

	public void configure(  ) {
		TcpContextServer contextServer  = new TcpContextServer("TcpApplServer", serverPort);
		CounterHandler counter          = new CounterHandler(resourceName);
		contextServer.addComponent(counter.getName(),counter);	
		contextServer.activate();   
 	}
	
	public void execute() throws Exception {
 		CounterClient client1 = new CounterClient("client1","localhost",serverPort);
		CounterClient client2 = new CounterClient("client2","localhost",serverPort);
		client1.sendValueOnConnection(msg1.toString());
		client2.sendValueOnConnection(msg1.toString());		
		client1.sendValueOnConnection(msg2.toString());
		client2.sendValueOnConnection(msg2.toString());		
	}

 
	public static void main( String[] args) throws Exception {		
		SharedCounterExampleMain sys = new SharedCounterExampleMain();
		sys.configure();
		sys.execute();
 
	}
}

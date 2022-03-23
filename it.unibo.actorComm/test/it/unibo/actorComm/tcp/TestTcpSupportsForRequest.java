package it.unibo.actorComm.tcp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.actorComm.common.NaiveApplHandler;
import it.unibo.actorComm.utils.CommSystemConfig;


public class TestTcpSupportsForRequest {
private TcpServer server;
public static final int testPort = 8112; 


	//@Before
	public void up() {
		CommSystemConfig.tracing = true;
	}
	
	@After
	public void down() {
		if( server != null ) server.deactivate();
	}	
	
	protected void startTheServer(String name) {
		server = new TcpServer(name, testPort, new NaiveApplHandler("naiveH") );
		server.activate();		
	}
	
	@Test 
	public void testSingleClient() {
		startTheServer("oneClientServer");
 		//Create a connection
		new ClientDoingRequest().doWork("client1","naiveH", 10 );		
		System.out.println("tesSingleClient BYE");
	}
	
	
	@Test 
	public void testManyClients() {
		startTheServer("manyClientsServer");
		new ClientDoingRequest().doWork("client1","naiveH",10);
		new ClientDoingRequest().doWork("client2","naiveH",1);
		new ClientDoingRequest().doWork("client3","naiveH",1);
		System.out.println("testManyClients BYE");
	}	
	

 
	
}

package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClientSupport;
import it.unibo.enablerCleanArch.supports.TcpServer;
import it.unibo.enablerCleanArchapplHandlers.NaiveApplHandler;


/*
 * Handler of messages received by the client on a connection
 * with the server
 */

/*
 * A client that performs a request on a connnection woth the server 
 * and waits for the answer
 */
class ClientForTestWithConn{
	public static boolean withserver = true;
	
	public void doWork( String name, int nattempts ) {
		try {
			Interaction2021 conn  = TcpClientSupport.connect("localhost", TestTcpSupportsWithConn.testPort,nattempts);
			String request = "hello_from_" + name;
			System.out.println(name + " | forward the request=" + request + " on conn:" + conn);	 
			conn.forward(request);
			String answer = conn.receiveMsg();
			System.out.println(name + " | receives the answer: " +answer );	
			assertTrue( answer.equals("answerTo_"+ request));
		} catch (Exception e) {
			System.out.println(name + " | ERROR " + e.getMessage());	
			if( withserver ) fail();
		}
	}
}

public class TestTcpSupportsWithConn {
private TcpServer server;
public static final int testPort = 8112; 


	//@Before
	public void up() {
	}
	
	@After
	public void down() {
		//if( server != null ) server.deactivate();
	}	
	
	protected void startTheServer(String name) {
		server = new TcpServer(name, testPort, new NaiveApplHandler("naiveH") );
		server.activate();		
	}
	
	//@Test 
	public void testSingleClient() {
		startTheServer("oneClientServer");
 		//Create a connection
		new ClientForTestWithConn().doWork("client1",10 );		
		System.out.println("tesSingleClient BYE");
	}
	
	
	@Test 
	public void testManyClients() {
		startTheServer("manyClientsServer");
		new ClientForTestWithConn().doWork("client1",10);
		new ClientForTestWithConn().doWork("client2",1);
		new ClientForTestWithConn().doWork("client3",1);
		System.out.println("testManyClients BYE");
	}	
	

	private void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}
	
}

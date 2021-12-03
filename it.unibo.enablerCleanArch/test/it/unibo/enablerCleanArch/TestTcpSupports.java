package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClient;
import it.unibo.enablerCleanArch.supports.TcpServer;


/*
 * Handler of messages received by the client on a connection
 * with the server
 */
class NaiveHandler extends ApplMessageHandler {
private static int count = 1;

	static NaiveHandler create() {
		return new NaiveHandler( "nh"+count++);
	}
	
	private NaiveHandler(String name) {
		super(name);
	}

	public void elaborate( String message ) {
		System.out.println(name+" | elaborates: "+message);
		try {
			conn.forward("answerTo_"+message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

/*
 * A client that performs a request on a connnection woth the server 
 * and waits for the answer
 */
class ClientForTest{
	public void doWork(String name) {
		try {
			Interaction2021 conn  = TcpClient.connect("locahost", TestTcpSupports.testPort);
			String request = "hello from" + name;
			conn.forward(request);
			String answer = conn.receiveMsg();
			System.out.println(name + " | receives the answer: " +answer );	
			assertTrue( answer.equals("answerTo_"+ request));
		} catch (Exception e) {
			System.out.println(name + " | ERROR " + e.getMessage());	
			fail();
		}
	}
}

public class TestTcpSupports {
private TcpServer server;
public static final int testPort = 8111; 


	@Before
	public void up() {
		System.out.println("up");
		//Create a server	
		server = new TcpServer("tcpTestServer",testPort, NaiveHandler.create());
	}
	
	@After
	public void down() {
		//System.out.println("down");	
		if( server != null ) server.deactivate();
	}	
	
	
	//@Test 
	public void testSingleClient() {
		server.activate();
		System.out.println("tesSingleClient");
		//Create a connection
		new ClientForTest().doWork("client1");
		//delay(3000);
		System.out.println("tesSingleClient BYE");
	}
	
	
	@Test 
	public void testManyClients() {
		server.activate();
		System.out.println("testManyClients");
		//Create a connection
		new ClientForTest().doWork("client1");
		new ClientForTest().doWork("client2");
		new ClientForTest().doWork("client3");
		//delay(3000);
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

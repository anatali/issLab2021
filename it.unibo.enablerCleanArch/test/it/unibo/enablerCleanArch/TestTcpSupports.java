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
/*
	static NaiveHandler create() {
		return new NaiveHandler( "nh"+count++);
	}
	
	private NaiveHandler(String name) {
		super(name);
	}
	public NaiveHandler(Interaction2021 conn) {
		super(conn);
	}*/

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
	public static boolean withserver = true;
	
	public void doWork( String name, int nattempts ) {
		try {
			Interaction2021 conn  = TcpClient.connect("localhost", TestTcpSupports.testPort,nattempts);
			//System.out.println(name + " | conn: " +conn );	
			String request = "hello from" + name;
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

public class TestTcpSupports {
private TcpServer server;
public static final int testPort = 8112; 


	//@Before
	public void up() {
	}
	
	@After
	public void down() {
		if( server != null ) server.deactivate();
	}	
	
	protected void startTheServer(String name) {
		server = new TcpServer(name,testPort, "it.unibo.enablerCleanArch.NaiveHandler" );
		server.activate();		
	}
	
	@Test 
	public void testClientNoServer() {
		System.out.println("testClientNoServer");
		ClientForTest.withserver = false;
		//Start the server later 
		/*
		new Thread() {
			public void run() {
				System.out.println("testClientNoServer run");
				delay(5000);
				System.out.println("testClientNoServer starts the server");
				startTheServer("serverLate");			
			}
		}.start();*/
		new ClientForTest().doWork("clientNoServer",3 );	
	}
	
	
	@Test 
	public void testSingleClient() {
		startTheServer("oneClientServer");
		System.out.println("tesSingleClient");
		//Create a connection
		new ClientForTest().doWork("client1",10 );		
		System.out.println("tesSingleClient BYE");
	}
	
	
	@Test 
	public void testManyClients() {
		startTheServer("manyClientsServer");
		System.out.println("testManyClients");
		//Create a connection
		new ClientForTest().doWork("client1",10);
		new ClientForTest().doWork("client2",1);
		new ClientForTest().doWork("client3",1);
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

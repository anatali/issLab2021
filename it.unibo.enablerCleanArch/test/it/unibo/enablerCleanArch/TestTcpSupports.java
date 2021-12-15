package it.unibo.enablerCleanArch;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClient;
import it.unibo.enablerCleanArch.supports.TcpServer;


/*
 * Handler of messages received by the client on a connection
 * with the server
 */
class NaiveHandler extends ApplMsgHandler {
	public NaiveHandler(String name) {
		super(name);
  	}
	@Override
	public void elaborate(String message, Interaction2021 conn) {
		System.out.println(name+" | elaborates: "+message);
		sendMsgToClient("answerTo_"+message, conn);	
	}
}

/*
 * A client that performs a request on a connnection woth the server 
 * and waits for the answer
 */
class ClientForTest{
 	
	public void doWork( String name, int ntimes, boolean withserver ) {
		try {
			Interaction2021 conn  = TcpClient.connect("localhost", TestTcpSupports.testPort,ntimes);
			//System.out.println(name + " | conn: " +conn );	
			String request = "hello_from_" + name;
			conn.forward(request);				//2
			String answer = conn.receiveMsg();	//3
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
		server = new TcpServer(name,testPort, new NaiveHandler("naiveH") );
		server.activate();		
	}
	
	@Test 
	public void testClientNoServer() {
		System.out.println("testClientNoServer");
		int numAttempts = 3;
		boolean withserver = false;
		new ClientForTest().doWork("clientNoServer",numAttempts,withserver );	
	}
	
	
	@Test 
	public void testSingleClient() {
		startTheServer("oneClientServer");
		System.out.println("tesSingleClient");
		//Create a connection
		boolean withserver = true;
		new ClientForTest().doWork("client1",10,withserver );		
		System.out.println("tesSingleClient BYE");
	}
	
	@Test 
	public void testManyClients() {
		startTheServer("manyClientsServer");
		System.out.println("testManyClients");
		//Create a connection
		boolean withserver = true;
		new ClientForTest().doWork("client1",10,withserver );
		new ClientForTest().doWork("client2",1,withserver);
		new ClientForTest().doWork("client3",1,withserver);
		System.out.println("testManyClients BYE");
	}	
	

<<<<<<< HEAD
 
=======
>>>>>>> ec913d676d3c6be52d27167764322c465fae8efe
	
}

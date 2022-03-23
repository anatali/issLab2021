package it.unibo.actorComm.tcp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.IApplMessage;

 
/*
 * A client that performs a request on a connnection with the server 
 * and waits for the answer
 */
public class ClientDoingRequest {
	public static boolean withserver = true;
	
	public void doWork( String name, String dest, int nattempts ) {
		try {
			Interaction2021 conn  = TcpClientSupport.connect("localhost", 
					                      TestTcpSupportsForRequest.testPort,nattempts);
			//String request = "hello_from_" + name;
 			IApplMessage request = CommUtils.buildRequest(name, "req", "hello", dest);
			System.out.println(name + " | forward the request=" + request + " on conn:" + conn);	 
			conn.forward(request.toString());
			String answer = conn.receiveMsg();
			System.out.println(name + " | receives the answer: " +answer );	
			assertTrue( answer.equals("answerTo_"+ request));
		} catch (Exception e) {
			System.out.println(name + " | ERROR " + e.getMessage());	
			if( withserver ) fail();
		}
	}

}

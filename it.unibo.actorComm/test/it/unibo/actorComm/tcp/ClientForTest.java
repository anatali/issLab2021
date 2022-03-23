package it.unibo.actorComm.tcp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.IApplMessage;

 
/*
 * A client that performs a request on a connnection woth the server 
 * and waits for the answer
 */
class ClientForTest{
private Interaction2021 conn;
	
	public void doWorkWithServerOff( String name, int ntimes  ) {
 		try {
			connect(ntimes);
			fail();  //non deve connttersi ...
		} catch (Exception e) {
			ColorsOut.outerr(name + " | ERROR (expected)" + e.getMessage());	
		}
	}
	
	protected void connect(int ntimes) throws Exception{
		conn  = TcpClientSupport.connect("localhost", TestTcpSupports.testPort,ntimes);
		
	}
	public void doWorkWithServerOn( String name, String dest, int ntimes  ) {
		try {
			connect(ntimes);                    //1
 			IApplMessage m = CommUtils.buildRequest(name, "req", "hello", dest);
			conn.forward(m.toString());				//2
			String answer = conn.receiveMsg();	//3
			ColorsOut.out(name + " | receives the answer: " +answer );	
			assertTrue( answer.equals("answerTo_"+ m));
		} catch (Exception e) {
			ColorsOut.outerr(name + " | ERROR " + e.getMessage());	
			fail();
		}
	}
}

package it.unibo.enablerCleanArch.tcp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.tcp.TcpClientSupport;

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
	public void doWorkWithServerOn( String name, int ntimes  ) {
		try {
			connect(ntimes);                    //1
 			String request = "hello_from_" + name;
			conn.forward(request);				//2
			String answer = conn.receiveMsg();	//3
			ColorsOut.out(name + " | receives the answer: " +answer );	
			assertTrue( answer.equals("answerTo_"+ request));
		} catch (Exception e) {
			ColorsOut.outerr(name + " | ERROR " + e.getMessage());	
			fail();
		}
	}
}

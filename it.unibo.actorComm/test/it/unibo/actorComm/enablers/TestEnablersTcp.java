package it.unibo.actorComm.enablers;

import static org.junit.Assert.assertTrue;

import org.junit.*;

import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.enablers.EnablerAsServer;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.actorComm.tcp.NaiveApplHandler;
import it.unibo.actorComm.utils.ColorsOut;



public class TestEnablersTcp {
	public static final int testPort = 8112; 	
	private EnablerAsServer srv; 
	 
	@Before
	public void setup() {
		srv = new EnablerAsServer("enblr",testPort,ProtocolType.tcp, new NaiveApplHandler("naiveH" ) );
		
 	}

	@After
	public void down() {
 	}
	
	@Test
    public void testEnablers() {
		srv.start();
		ProxyAsClient pxy = new ProxyAsClient("pxy", "localhost", ""+testPort, ProtocolType.tcp);
//		pxy.sendCommandOnConnection("hello");
		String req = "arequest";
		String answer = pxy.sendRequestOnConnection( req );
		ColorsOut.out(answer, ColorsOut.MAGENTA);
		assertTrue( answer.equals("answerTo_"+ req));
	}
}

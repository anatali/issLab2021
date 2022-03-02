package it.unibo.comm2022.tcp.enablers;

import static org.junit.Assert.assertTrue;

import org.junit.*;

import it.unibo.comm2022.ProtocolType;
import it.unibo.comm2022.enablers.EnablerAsServer;
import it.unibo.comm2022.proxy.ProxyAsClient;
import it.unibo.comm2022.tcp.NaiveApplHandler;
import it.unibo.comm2022.utils.ColorsOut;



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

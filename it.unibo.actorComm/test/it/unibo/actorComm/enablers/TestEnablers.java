package it.unibo.actorComm.enablers;

import static org.junit.Assert.assertTrue;
import org.junit.*;
import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.common.NaiveApplHandler;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.IApplMessage;



public class TestEnablers {
	public static final int testPort = 8112; 	
	private EnablerServer srv; 
	 
	@Before
	public void setup() {
		srv = new EnablerServer("enblr",testPort,ProtocolType.tcp, 
				          new NaiveApplHandler("naiveH" ) );
		
 	}

	@After
	public void down() {
 	}
	
	@Test
    public void testEnablers() {
		srv.start();
		ProxyAsClient pxy = new ProxyAsClient("pxy", "localhost", ""+testPort, ProtocolType.tcp);
//		pxy.sendCommandOnConnection("hello");
		String req     = "arequest";
		IApplMessage m = CommUtils.buildRequest("tester", "req", req, "naiveH");
		String answer  = pxy.sendRequestOnConnection( m.toString() );
		ColorsOut.outappl(answer, ColorsOut.MAGENTA);
		assertTrue( answer.equals("answerTo_"+ m));
	}
}

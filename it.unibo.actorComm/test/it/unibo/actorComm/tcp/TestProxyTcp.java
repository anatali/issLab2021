package it.unibo.actorComm.tcp;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.common.NaiveApplHandler;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.IApplMessage;

public class TestProxyTcp {
public static final int testPort = 8112; 
	private TcpServer server;

	@Before
	public void up() {
		System.out.println(" =============== ACTIVATING SERVER  " );
		server = new TcpServer("tcpServer",testPort, new NaiveApplHandler("naiveH") );		
		server.activate();		
		
		
	}
	
	@After
	public void down() {
		if( server != null ) server.deactivate();
		System.out.println(" =============== SERVER DEACTIVATED" );
	}	

	@Test
	public void useProxy() {
		ProxyAsClient pxy = new ProxyAsClient("pxy", "localhost", ""+testPort, ProtocolType.tcp);
		//pxy.sendCommandOnConnection("hello");
 		IApplMessage req = CommUtils.buildRequest("tester", "req", "arequest", "naiveH");

		String answer = pxy.sendRequestOnConnection( req.toString() );
		ColorsOut.out(answer, ColorsOut.MAGENTA);
		assertTrue( answer.equals("answerTo_"+ req));
 	}
}

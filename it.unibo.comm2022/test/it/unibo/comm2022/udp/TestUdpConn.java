package it.unibo.comm2022.udp;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.comm2022.ProtocolType;
import it.unibo.comm2022.proxy.ProxyAsClient;
import it.unibo.comm2022.tcp.NaiveApplHandler;
import it.unibo.comm2022.tcp.TcpServer;
import it.unibo.comm2022.utils.BasicUtils;
import it.unibo.comm2022.utils.ColorsOut;
import it.unibo.comm2022.utils.CommUtils;

public class TestUdpConn {
	public static final int testPort = 8007; 
	private UdpServer server;
	private ProxyAsClient pxy;
	
	@Before
	public void up() {
		System.out.println(" =============== ACTIVATING SERVER  " );
		server = new UdpServer("udpSrc",testPort, new NaiveApplHandler("naiveH") );
		server.activate();		
	}
	
	@After
	public void down() {
		if( pxy != null )  pxy.close();
		if( server != null ) server.deactivate();
		System.out.println(" =============== SERVER DEACTIVATED" );
	}	

	@Test
	public void useUdpConn() {
		pxy = new ProxyAsClient("pxy", "localhost", ""+testPort, ProtocolType.udp);
//		pxy.sendCommandOnConnection("hello");
		String req    = "arequest";
		String answer = pxy.sendRequestOnConnection( req );
		ColorsOut.outappl(answer, ColorsOut.BgYellow);
		assertTrue( answer.equals("answerTo_"+ req));
		BasicUtils.delay(1000);

	}
}

package it.unibo.comm2022.udp;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.comm2022.ProtocolType;
import it.unibo.comm2022.common.NaiveApplHandler;
import it.unibo.comm2022.proxy.ProxyAsClient;
import it.unibo.comm2022.tcp.TcpServer;
import it.unibo.comm2022.utils.BasicUtils;
import it.unibo.comm2022.utils.ColorsOut;
import it.unibo.comm2022.utils.CommUtils;

public class TestUdpConn {
	public static final int testPort = 8007; 
	private UdpServer server;
	private ProxyAsClient pxy1,pxy2;
	
	@Before
	public void up() {
		System.out.println(" =============== ACTIVATING SERVER  " );
		server = new UdpServer("udpSrc",testPort, new NaiveApplHandler("naiveH") );
		server.activate();		
	}
	
	@After
	public void down() {
		if( pxy1 != null )  pxy1.close();
		if( pxy2 != null )  pxy2.close();
		if( server != null ) server.deactivate();
		System.out.println(" =============== SERVER DEACTIVATED" );
	}	

	@Test
	public void useUdpConn() {
		//pxy1 = new ProxyAsClient("pxy1", "localhost", ""+testPort, ProtocolType.udp);
		//pxy2 = new ProxyAsClient("pxy2", "localhost", ""+testPort, ProtocolType.udp);
//		pxy.sendCommandOnConnection("hello");
		new Thread() {
			public void run() {
				String req1    = "arequest1";
				pxy1 = new ProxyAsClient("pxy1", "localhost", ""+testPort, ProtocolType.udp);
				String answer1 = pxy1.sendRequestOnConnection( req1 );
				ColorsOut.outappl("answer1="+answer1, ColorsOut.BgYellow);				
			}
		}.start();
		new Thread() {
			public void run() {
				String req2    = "arequest2";
				pxy2 = new ProxyAsClient("pxy2", "localhost", ""+testPort, ProtocolType.udp);
				String answer2 = pxy2.sendRequestOnConnection( req2 );
				ColorsOut.outappl("answer2="+answer2, ColorsOut.BgYellow);				
			}
		}.start();

		//assertTrue( answer.equals("answerTo_"+ req));
		BasicUtils.delay(2000);

	}
}

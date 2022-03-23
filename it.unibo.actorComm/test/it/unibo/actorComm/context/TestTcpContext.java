package it.unibo.actorComm.context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;


public class TestTcpContext {
	private int ctxServerPort   = 7070;
	private TcpContextServer contextServer ;
	
	private IApplMessage info = new ApplMessage(
		      "msg( info, dispatch, main, applActor, hello, 1 )" );
	
	@Before
	public void up() {
		CommSystemConfig.tracing = false;
// 		SimpleApplHandler applH = new SimpleApplHandler("applH");
 		SimpleApplActor applH = new SimpleApplActor("applActor");
 		
		contextServer = new TcpContextServer("TcpContextServer",  ctxServerPort );
 		contextServer.addComponent(applH.getName(),applH);	
 		contextServer.activate();    		
	}
	
	@After
	public void down() {
		CommUtils.delay(1000);
		contextServer.deactivate();
	}
	
	@Test
	public void sendDispatchToHandler() {
		ProxyAsClient client1 = new ProxyAsClient("client1","localhost", ""+ctxServerPort, ProtocolType.tcp);
		client1.sendCommandOnConnection(info.toString()); 
	}

}

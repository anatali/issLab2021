package it.unibo.actorComm.context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.actorComm.utils.BasicUtils;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;


public class TestContext {
	private int ctxServerPort   = 7070;
	private EnablerContextForActors contextServer ;
	
	private IApplMessage info = new ApplMessage(
		      "msg( info, dispatch, main, applActor, hello, 1 )" );
	
	@Before
	public void up() {
		CommSystemConfig.tracing     = false;
		CommSystemConfig.protcolType = ProtocolType.tcp;
// 		SimpleApplHandler applH = new SimpleApplHandler("applH");
 		SimpleApplActor applH = new SimpleApplActor("applActor");
 		
 		BasicUtils.waitTheUser();
 		
		contextServer = new EnablerContextForActors("ctxEnblr",  ""+ctxServerPort, 
				CommSystemConfig.protcolType, new ContextMsgHandler("ctxH"));
// 		contextServer.addComponent(applH.getName(),applH);	
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

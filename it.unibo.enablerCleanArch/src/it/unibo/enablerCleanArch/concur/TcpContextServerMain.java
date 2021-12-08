package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.SysMessageHandler;
import it.unibo.enablerCleanArch.supports.TcpContextServer;

public class TcpContextServerMain {

	public static void main( String[] args) throws Exception {
		int port = 7070;
		TcpContextServer server = new TcpContextServer("TcpApplServer", port);
		SysMessageHandler h  = server.getHandler();
		
		ApplMessageHandler  naiveH = new NaiveApplHandler("naiveH");
		h.registerHandler("componentA",naiveH);
		h.registerHandler("componentB",naiveH);
		
		server.activate();
		
		ComponentAClient client = new ComponentAClient("client","localhost",port);
		ApplMessage msg1        = new ApplMessage("msg( m1, dispatch, main, componentA, hello1, 1 )");
		ApplMessage msg2        = new ApplMessage("msg( m1, dispatch, main, componentB, hello2, 2 )");
		client.getConn().forward(msg1.toString());
		client.getConn().forward(msg2.toString());
	}

}

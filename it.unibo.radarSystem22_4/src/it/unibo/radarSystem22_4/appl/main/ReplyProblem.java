package it.unibo.radarSystem22_4.appl.main;

import it.unibo.radarSystem22_4.appl.handler.RequestHandler;
import it.unibo.radarSystem22_4.comm.ProtocolType;
import it.unibo.radarSystem22_4.comm.context.TcpContextServer;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMsgHandler;
import it.unibo.radarSystem22_4.comm.proxy.ProxyAsClient;
import it.unibo.radarSystem22_4.comm.tcp.TcpServer;
import it.unibo.radarSystem22_4.comm.utils.CommUtils;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMessage;

public class ReplyProblem {
private int port = 8078;
 
	public void doJob() {
		//Attivo un server con Handler che attiva thread
		IApplMsgHandler h = new RequestHandler("rh");
		 
		TcpContextServer server  = new TcpContextServer( "srv", port );
		
		server.addComponent("h", h);
		server.activate();
		
		ProxyAsClient pxy = new ProxyAsClient("pxy", "localhost", ""+port, ProtocolType.tcp);
		//Attivo due Thread di richesta
 		
		new Thread() {
			public void run() {
				IApplMessage m = CommUtils.buildRequest("main", "req", "r1","h");
				pxy.sendRequestOnConnection(m.toString());
			}
		}.start();
		new Thread() {
			public void run() {
				IApplMessage m = CommUtils.buildRequest("main", "req", "r2","h");
				pxy.sendRequestOnConnection(m.toString());
			}
		}.start();
		 
	}
	
	public static void main( String[] args) throws Exception {
		//ColorsOut.out("Please set RadarSystemConfig.pcHostAddr in RadarSystemConfig.json");
		new ReplyProblem().doJob( );
 	}

	
}

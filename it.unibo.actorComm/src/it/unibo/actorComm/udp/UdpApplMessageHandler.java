package it.unibo.actorComm.udp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import it.unibo.actorComm.interfaces.IApplMsgHandler;
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;

/*
 * Ente attivo per la ricezione di messaggi su una connessione Interaction2021
 */
public class UdpApplMessageHandler extends Thread{
private  IApplMsgHandler handler ;
private Interaction2021 conn;
  
 
public UdpApplMessageHandler(  IApplMsgHandler handler, Interaction2021 conn ) {
		this.handler = handler;
		this.conn    = conn;
         this.start();
	}
 	
	@Override 
	public void run() {
		String name = handler.getName();
		try {
			ColorsOut.out( "UdpApplMessageHandler | STARTS with handler=" + name + " conn=" + conn, ColorsOut.BLUE );
			while( true ) {
				//ColorsOut.out(name + " | waits for message  ...");
			    String msg = conn.receiveMsg();
			    ColorsOut.out(name + "  | UdpApplMessageHandler received:" + msg + " handler="+handler, ColorsOut.BLUE );
			    if( msg == null || msg.equals(UdpConnection.closeMsg) ) {
			    	conn.close();
			    	break;
			    } else{ 
 			    	IApplMessage m = new ApplMessage(msg);
 			    	handler.elaborate( m, conn );   //chiama  ctxH
			    }
			}
			ColorsOut.out(name + " | BYE", ColorsOut.BLUE   );
		}catch( Exception e) {
			ColorsOut.outerr( name + " UdpApplMessageHandler | ERROR:" + e.getMessage()  );
		}	
	}
}
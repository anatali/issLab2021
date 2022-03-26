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
//private Map<String,Interaction2021> requestConnectionsMap;  
 
/*
 * Gestione dei messaggi in ingresso sulla connessione
 */
public UdpApplMessageHandler(  IApplMsgHandler handler, Interaction2021 conn ) {
		this.handler = handler;
		this.conn    = conn;
		//requestConnectionsMap = new ConcurrentHashMap<String,Interaction2021>();
        this.start();
	}
 	
	@Override 
	public void run() {
		String name = handler.getName();
		try {
			ColorsOut.outappl( "UdpApplMessageHandler | STARTS with handler=" + name + " conn=" + conn, ColorsOut.MAGENTA );
			while( true ) {
				//ColorsOut.out(name + " | waits for message  ...");
			    String msg = conn.receiveMsg();
			    ColorsOut.outappl(name + "  | UdpApplMessageHandler received:" + msg + " handler="+handler, ColorsOut.GREEN );
			    if( msg == null || msg.equals(UdpConnection.closeMsg) ) {
			    	conn.close();
			    	break;
			    } else{ 
 			    	IApplMessage m = new ApplMessage(msg);
//			    	if( m.isRequest() ) {
//			    		String sender = m.msgSender();
//			    		Interaction2021 yetconn = requestConnectionsMap.get(sender);
//			    		if( yetconn == null ) requestConnectionsMap.put(sender, conn);
//			    		//Memorizzo connessione per nome sender
//			    	}else { //Invia il msg all'attore
//			    		
//			    	}
			    	handler.elaborate( m, conn );   //chiama  ctxH
			    }
			}
			ColorsOut.out(name + " | BYE"   );
		}catch( Exception e) {
			ColorsOut.outerr( name + " UdpApplMessageHandler | ERROR:" + e.getMessage()  );
		}	
	}
}

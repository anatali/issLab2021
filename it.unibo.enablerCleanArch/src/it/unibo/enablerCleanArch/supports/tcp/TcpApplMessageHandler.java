package it.unibo.enablerCleanArch.supports.tcp;

import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;

/*
 * Ente attivo per la ricezione di messaggi su una connessione Interaction2021
 */
public class TcpApplMessageHandler extends Thread{
private  IApplMsgHandler handler ;
private Interaction2021 conn;

public TcpApplMessageHandler(  IApplMsgHandler handler, Interaction2021 conn ) {
		this.handler = handler;
		this.conn    = conn;
 		this.start();
	}
 	
	@Override 
	public void run() {
		String name = handler.getName();
		try {
			ColorsOut.out( "TcpApplMessageHandler | STARTS with handler=" + name + " conn=" + conn );
			while( true ) {
				//ColorsOut.out(name + " | waits for message  ...");
			    String msg = conn.receiveMsg();
			    //ColorsOut.out(name + "  | TcpApplMessageHandler received:" + msg );
			    if( msg == null ) {
			    	conn.close();
			    	break;
			    } else{ handler.elaborate( msg, conn ); }
			}
			ColorsOut.out(name + " | BYE"   );
		}catch( Exception e) {
			ColorsOut.outerr( name + "  | ERROR:" + e.getMessage()  );
		}	
	}
}

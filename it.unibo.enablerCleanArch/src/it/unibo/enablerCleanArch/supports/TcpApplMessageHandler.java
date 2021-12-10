package it.unibo.enablerCleanArch.supports;
 
/*
 * Ente attivo per la ricezione di messaggi su una connessione Interaction2021
 */
public class TcpApplMessageHandler extends Thread{
private  ApplMessageHandler handler ;
private Interaction2021 conn;

public TcpApplMessageHandler(  ApplMessageHandler handler, Interaction2021 conn ) {
		this.handler = handler;
		this.conn    = conn;
 		this.start();
	}
 	
	@Override 
	public void run() {
		String name          = handler.getName();
		//Interaction2021 conn = handler.getConn() ;
		try {
			Colors.out(name + " | TcpApplMessageHandler STARTS  conn=" + conn );
			while( true ) {
				//Colors.out(name + " | waits for message  ...");
			    String msg = conn.receiveMsg();
			    Colors.out(name + "  | received:" + msg );
			    if( msg == null ) {
			    	conn.close();
			    	break;
			    } else{ handler.elaborate( msg, conn ); }
			}
			Colors.out(name + " | BYE"   );
		}catch( Exception e) {
			Colors.outerr( name + "  | ERROR:" + e.getMessage()  );
		}	
	}
}

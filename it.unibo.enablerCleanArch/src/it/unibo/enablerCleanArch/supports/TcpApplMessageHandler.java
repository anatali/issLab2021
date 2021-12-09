package it.unibo.enablerCleanArch.supports;
 
/*
 * Ente attivo per la ricezione di messaggi su una connessione Interaction2021
 */
public class TcpApplMessageHandler extends Thread{
private  ApplMessageHandler handler ;


	public TcpApplMessageHandler(  ApplMessageHandler handler ) {
		this.handler = handler;
 		this.start();
	}
 	
	@Override
	public void run() {
		String name          = handler.getName();
		Interaction2021 conn = handler.getConn() ;
		try {
			//Colors.out(name + " | TcpApplMessageHandler STARTS  "  );
			while( true ) {
				//Colors.out(name + " | waits for message on ...");
			    String msg = conn.receiveMsg();
			    //Colors.out(name + "  | received:" + msg );
			    if( msg == null ) {
			    	conn.close();
			    	break;
			    } else{ handler.elaborate( msg ); }
			}
			Colors.out(name + " | BYE"   );
		}catch( Exception e) {
			Colors.outerr( name + "  | ERROR:" + e.getMessage()  );
		}	
	}
}

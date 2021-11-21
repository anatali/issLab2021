package it.unibo.enablerCleanArch.supports;
 
/*
 * Ente attivo per la ricezione di messaggi su una connessione Interaction2021
 */
public class TcpApplMessageHandler {
  	
	public TcpApplMessageHandler( ApplMessageHandler handler ) {
 		startUp(handler);
	}
 	
	private void startUp( ApplMessageHandler handler ) {
		new Thread() {
			public void run() {
				String name          = handler.getName();
				Interaction2021 conn = handler.getConn() ;
				try {
					System.out.println(name + " | STARTS ...");
					while( true ) {
						//System.out.println(name + " | waits for message on ...");
					    String msg = conn.receiveMsg();
						//System.out.println(name + " | received:" + msg );
					    if( msg == null ) {
					    	conn.close();
					    	break;
					    } else{ handler.elaborate( msg ); }
					}
				}catch( Exception e) {
					System.out.println(name + " | ERROR:" + e.getMessage() );
				}
			}
		}.start();	
	}
 
}

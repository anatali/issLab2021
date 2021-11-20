package it.unibo.enablerCleanArch.supports;
 
/*
 * Ente attivo per la ricezione di messaggi su una connessione Interaction2021
 */
public class TcpMessageHandler {

	//private Interaction2021 conn;
	//protected String name  ;
	
	public TcpMessageHandler( ApplMessageHandler handler ) {
		//this.name = handler.getName();
		//this.conn = handler.getConn(conn);
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
						System.out.println(name + " | received:" + msg );
					    if( msg == null ) conn.close();
					    handler.elaborate( msg );
					}
				}catch( Exception e) {
					System.out.println(name + " | ERROR:" + e.getMessage() );
				}
			}
		}.start();	
	}
 
}

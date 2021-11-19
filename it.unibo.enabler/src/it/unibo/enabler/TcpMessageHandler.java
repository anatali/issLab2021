package it.unibo.enabler;
 
/*
 * Ente attivo per la ricezione di messaggi su una connessione IConnInteraction
 */
public   class TcpMessageHandler {

	private Interaction2021 conn;
	protected String name  ;
	
	public TcpMessageHandler( String name, Interaction2021 conn, ApplMessageHandler handler ) {
		this.name = name;
		this.conn = conn;
		startUp(handler);
	}
	
	
	private void startUp( ApplMessageHandler handler ) {
		new Thread() {
			public void run() {
				try {
					System.out.println(name + " | STARTS ...");
					while( true ) {
						//System.out.println(name + " | waits for message on ...");
					    String msg = conn.receiveMsg();
						//System.out.println(name + " | received:" + msg );
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

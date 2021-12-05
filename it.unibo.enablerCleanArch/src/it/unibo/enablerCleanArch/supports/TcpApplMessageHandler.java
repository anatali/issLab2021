package it.unibo.enablerCleanArch.supports;
 
/*
 * Ente attivo per la ricezione di messaggi su una connessione Interaction2021
 */
public class TcpApplMessageHandler extends Thread{
private  ApplMessageHandler handler ;

	public TcpApplMessageHandler( ApplMessageHandler handler ) {
		this.handler = handler;
 		this.start();
	}
 	
	@Override
	public void run() {
		String name          = handler.getName();
		Interaction2021 conn = handler.getConn() ;
		try {
			Colors.out("TcpApplMessageHandler | STARTS with handler:" + name );
			while( true ) {
				//System.out.println(name + " | waits for message on ...");
			    String msg = conn.receiveMsg();
			    //Colors.out("TcpApplMessageHandler | received:" + msg );
			    if( msg == null ) {
			    	conn.close();
			    	break;
			    } else{ handler.elaborate( msg ); }
			}
			Colors.out("TcpApplMessageHandler | BYE"   );
		}catch( Exception e) {
			Colors.outerr("TcpApplMessageHandler | ERROR:" + e.getMessage()  );
		}	
	}
}

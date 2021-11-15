package it.unibo.enabler;

import java.net.ServerSocket;
import it.unibo.is.interfaces.protocols.IConnInteraction;
import java.net.Socket;

public abstract class TcpEnabler extends Thread implements IEnabler {
private IConnInteraction connSupport;
private ServerSocket serversock;
private int port;
protected String name;

	public TcpEnabler( String name, int port ) throws Exception {
		this.name      = name;
		this.port      = port;
		serversock     = new ServerSocket( port );
		this.start();
	}
	
	public int getPort() { return port; }
	
	@Override
	public void run() {
		try {
			while( true ) {
				System.out.println(name + " | waits on port=" + port);	 
				connSupport    = waitConn( serversock );
				//work();
				//Attivare work in un nuovo Thread? Se si ci sono molti 'padroni'
				activateWork( connSupport );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	protected void activateWork(IConnInteraction connSupport) {
		new Thread() {
			public void run() {
				work( connSupport );
			}
		}.start();
	}
	
	protected abstract void elaborate( String message );
	
	/*
	 * Attende la richiesta di UNA SOLA connessione (da parte del Controller)
	 */
	private IConnInteraction waitConn( ServerSocket serverSocket ) throws Exception{ 
		//System.out.println(name + " | waitConn on socket: " + serverSocket);
		int timeOut = 600000;  //msecs
		serverSocket.setSoTimeout(timeOut);
		Socket sock = serverSocket.accept();	
		return new TcpConnSupport(sock) ;
	}
		
	private void work(IConnInteraction connSupport) {
		try {
			while( true ) {
				//System.out.println(name + " | waits for message on ...");
			    String msg = connSupport.receiveALine();
				//System.out.println(name + " | received:" + msg );
				elaborate( msg );
			}
		}catch( Exception e) {
			
		}
	}
 	
}

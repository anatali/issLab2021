package it.unibo.enabler;

import java.io.IOException;
import java.net.ServerSocket;
import it.unibo.is.interfaces.protocols.IConnInteraction;
import java.net.Socket;

public class Enabler {
	
	public void startUp( int port) throws Exception {
		ServerSocket sock = new ServerSocket( port );
		IConnInteraction conn = waitConn( sock );
		//hlServerCommSupport = hlComm( conn );
	}
	
	
	private IConnInteraction waitConn( ServerSocket sock ) { 
		return null; 
	}
	
	
	private void work() {}
	
	
	public static void main( String[] args) throws Exception {
		Enabler sys = new Enabler();
		sys.startUp( 8010 );
		sys.work();
		
	}
	
	
}

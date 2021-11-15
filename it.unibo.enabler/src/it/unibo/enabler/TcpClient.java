package it.unibo.enabler;

import java.net.Socket;
import it.unibo.is.interfaces.protocols.IConnInteraction;

public class TcpClient {
	private IConnInteraction connSupport;

	public TcpClient( String host, int port ) throws Exception {
		Socket socket =  new Socket( host, port );	
		connSupport   =  new TcpConnSupport( socket );
	}
	
	public void forward( String msg ) throws Exception {
		connSupport.sendALine(msg);
	}
	

}

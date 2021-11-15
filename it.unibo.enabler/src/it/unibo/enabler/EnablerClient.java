package it.unibo.enabler;

import java.net.Socket;
import it.unibo.is.interfaces.protocols.IConnInteraction;

public class EnablerClient {
	private IConnInteraction connSupport;

	public EnablerClient( String host, IEnabler e ) throws Exception {
		//System.out.println( "EnablerClient | port=" + e.getPort() );	 
		Socket socket =  new Socket( host, e.getPort() );	
		connSupport   =  new TcpConnSupport( socket );
		//System.out.println( "EnablerClient | connSupport  " + connSupport );	 
	}
	
	public void forward( String msg ) throws Exception {
		//System.out.println( "EnablerClient | forward  " + msg );	 
		connSupport.sendALine(msg);
	}
	

}

package it.unibo.radarWithEnabler;

import java.net.Socket;

import it.unibo.enabler.TcpConnSupport;
import it.unibo.is.interfaces.protocols.IConnInteraction;

public class EnablerClient {
	private Interaction2021 connSupport;

	public EnablerClient( String host, int port ) throws Exception {
		System.out.println( "EnablerClient | host= " + host + " port=" + port );	 
		Socket socket =  new Socket( host, port );	
		connSupport   =  new TcpConnSupport( socket );
		//System.out.println( "EnablerClient | connSupport  " + connSupport );	 
	}
	
	public void forward( String msg ) throws Exception {
		//System.out.println( "EnablerClient | forward  " + msg );	 
		connSupport.sendALine(msg);
	}
	

}

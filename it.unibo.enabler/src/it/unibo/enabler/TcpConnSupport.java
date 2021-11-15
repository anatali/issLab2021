package it.unibo.enabler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import it.unibo.is.interfaces.protocols.IConnInteraction;

public class TcpConnSupport implements IConnInteraction{
private DataOutputStream outputChannel;
private BufferedReader inputChannel;

	public TcpConnSupport( Socket socket  ) throws Exception {
		OutputStream outStream 	       = socket.getOutputStream();
		InputStream inStream  	       = socket.getInputStream();
		outputChannel                  =  new DataOutputStream(outStream);
		inputChannel                   =  new BufferedReader( new InputStreamReader( inStream ) );		
	}
	
	@Override
	public void sendALine(String msg) throws Exception {
		//System.out.println( "TcpConnSupport | sendALine  on " + outputChannel);	 
		outputChannel.writeBytes( msg+"\n" );	
		outputChannel.flush();
		//System.out.println( "TcpConnSupport | has sent   " + msg );	 
	}

	@Override
	public void sendALine(String msg, boolean isAnswer) throws Exception {}

	@Override
	public String receiveALine() throws Exception {
 		try {
			//socket.setSoTimeout(timeOut)
			String	line = inputChannel.readLine() ; //blocking =>
 			return line;		
		} catch ( Exception e   ) {
	 		System.out.println( "TcpConnSupport | receiveALine ERROR  " + e.getMessage() );	 		
			throw e;
		}		
	}

	@Override
	public void closeConnection() throws Exception {}

}

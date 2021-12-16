package it.unibo.enablerCleanArch.supports;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
 

public class TcpConnection implements Interaction2021{
private DataOutputStream outputChannel;
private BufferedReader inputChannel;
private Socket socket;

	public TcpConnection( Socket socket  ) throws Exception {
		this.socket                    = socket;
		OutputStream outStream 	       = socket.getOutputStream();
		InputStream inStream  	       = socket.getInputStream();
		outputChannel                  =  new DataOutputStream(outStream);
		inputChannel                   =  new BufferedReader( new InputStreamReader( inStream ) );		
	}
	
	@Override
	public void forward(String msg)  throws Exception {
		//System.out.println( "TcpConnSupport | sendALine  on " + outputChannel);	 
		try {
			outputChannel.writeBytes( msg+"\n" );
			outputChannel.flush();
			//Colors.out( "TcpConnSupport | has sent   " + msg );	 
		} catch (IOException e) {
			//Colors.outerr( "TcpConnection | sendALine ERROR " + e.getMessage());	 
			throw e;
		}	
	}

	@Override
	public String request(String msg)  throws Exception {
		forward(  msg );
		String answer = receiveMsg();
		return answer;
	}
	
	@Override
	public void reply(String reqid) throws Exception {
	} 
	
	@Override
	public String receiveMsg()  {
 		try {
			//socket.setSoTimeout(timeOut)
			String	line = inputChannel.readLine() ; //blocking =>
 			return line;		
		} catch ( Exception e   ) {
			Colors.outerr( "TcpConnection | receiveMsg ERROR  " + e.getMessage() );	
	 		return null;
		}		
	}

	@Override
	public void close() { 
		try {
			socket.close();
		} catch (IOException e) {
			Colors.outerr( "TcpConnection | close ERROR " + e.getMessage());	
		}
	}



}

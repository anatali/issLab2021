package unibo.comm22.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import unibo.comm22.interfaces.Interaction2021;
import unibo.comm22.utils.ColorsOut;
 
  

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
		sendALine( msg );
	}

	@Override
	public String request(String msg)  throws Exception { //Bloccante
		forward(  msg );
		String answer = receiveMsg();
		return answer;
	}
	
 	
	@Override
	public void reply(String msg) throws Exception {
		forward(msg);
	} 
	
	@Override
	public String receiveMsg() throws Exception {
		return receiveALine();
	}

	@Override
	public void close() throws Exception { 
		closeConnection();
	}
	
	protected String receiveALine() throws Exception {
		String	line = inputChannel.readLine() ; //blocking =>
 		return line;		
	}
	
	protected void sendALine(String msg) throws Exception {
		//ColorsOut.out( "TcpConnection | sendALine  " + msg + " on " + outputChannel, ColorsOut.ANSI_YELLOW );
		try {
			outputChannel.writeBytes( msg+"\n" );
			outputChannel.flush();
			//ColorsOut.outappl( "TcpConnection | has sent   " + msg, ColorsOut.CYAN );	 
		} catch (Exception e) {
			ColorsOut.outerr( "TcpConnection | sendALine " + msg + "  ERROR:" + e.getMessage());	 
			throw e;  //propago la eccezione (ad es. a un NodeProxy)
	    }	
	}
	protected void closeConnection() throws Exception {
		socket.close();
		ColorsOut.out( "TcpConnection | CLOSED  ", ColorsOut.BLUE );
	}
 
}

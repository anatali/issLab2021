package lowLevelComms

import java.net.Socket
import java.io.OutputStream
import java.io.InputStream
import java.io.DataOutputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.SocketException
import it.unibo.`is`.interfaces.protocols.IConnInteraction  //WARNING !!! 

class tcpConnSupport( val socket: Socket ) : IConnInteraction {
 var outStream	   : OutputStream
 var inStream      : InputStream
 var outputChannel : DataOutputStream
 var inputChannel  : BufferedReader
	
	init{
			outStream 	 = socket.getOutputStream()
			inStream  	  = socket.getInputStream()
			outputChannel =  DataOutputStream(outStream);
			inputChannel  =  BufferedReader( InputStreamReader( inStream ) );	
		
	}
	
	override fun sendALine(  msg : String ){
		outputChannel.writeBytes( msg+"\n" );	
		outputChannel.flush();
		//println( "SocketTcpConnSupport | has written ... $msg  on ${socket.getLocalPort()}"  );		
	}
	
	override fun receiveALine( ) : String {
 		try {
			//socket.setSoTimeout(timeOut)
			val	line = inputChannel.readLine()  //blocking =>
			//println( "SocketTcpConnSupport | has read ... $line  on ${socket.getLocalPort()}"  )
			return line;		
		} catch ( e : SocketException ) {
	 		println( "SocketTcpConnSupport | receiveALine timeOut on ${socket.getLocalPort()}" );	 		
			throw e;
		}		
	}
	
	override fun sendALine(   msg : String,  isAnswer : Boolean )  {}
 	override fun  closeConnection( ){}  

	
}
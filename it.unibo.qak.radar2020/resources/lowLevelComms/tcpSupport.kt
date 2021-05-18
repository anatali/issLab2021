package lowLevelComms

import java.net.ServerSocket
import java.net.Socket

class tcpSupport {
	
	fun connectAsReceiver( portNum : Int) : ServerSocket {
		val serverSocket = ServerSocket( portNum )
		return serverSocket
	}
	
	fun acceptAConnection( serverSocket : ServerSocket ) : Socket{
		val timeOut = 600000  //msecs
		serverSocket.setSoTimeout(timeOut)
		val socket = serverSocket.accept()
		return socket
	}
	
	fun closeConnection( serverSocket : ServerSocket ){
		serverSocket.close()
	}
	
	fun connectAsClient( hostName : String,  port : Int) : Socket{
		val socket =  Socket( hostName, port )
		return socket
	}
	
}

val tecnoSupport  : tcpSupport = tcpSupport()  //Visible in the package

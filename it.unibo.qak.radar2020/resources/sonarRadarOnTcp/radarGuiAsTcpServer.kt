package sonarRadarOnTcp
/*
 radarGuiAsTcpServer.kt
 ------------------------------------------------------------------------------------------
 A scheme for a radarGui that works as a TCP server
 The structure of the code arises from the technology-support (socket in java.net libray)
 ------------------------------------------------------------------------------------------
*/ 
import it.unibo.kactor.MsgUtil
import java.net.ServerSocket
import it.unibo.`is`.interfaces.protocols.IConnInteraction  //WARNING !!! 
import lowLevelComms.tcpConnSupport
import highLevelComms.hlComm

var hlServerCommSupport : hlComm?  =  null

fun serverStartUp( port:Int ){
	println("server | init ...  ")
	val serverSocket    = lowLevelComms.tecnoSupport.connectAsReceiver( port )
	val conn            = waitForConn( serverSocket )
	hlServerCommSupport = hlComm( conn )
}

fun waitForConn( serverSocket : ServerSocket ) : IConnInteraction{
	val socket       = lowLevelComms.tecnoSupport.acceptAConnection(serverSocket)
	println("server | new connection set  ")
	return tcpConnSupport( socket )
}

fun serverWork(){
 	println("server | waits for message ...")
 	val msg = hlServerCommSupport!!.receive()
	println("server | received: $msg" )
}

fun main() {
    println("server | BEGIN")
	serverStartUp( 8010 )
 	serverWork()
    println("server | END")
}
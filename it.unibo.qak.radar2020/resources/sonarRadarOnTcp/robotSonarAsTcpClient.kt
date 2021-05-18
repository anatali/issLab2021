package sonarRadarOnTcp
/*
 robotSonarAsTcpClient.kt
*/ 
import java.net.Socket
import java.net.ServerSocket
import it.unibo.kactor.ApplMessage
import it.unibo.kactor.MsgUtil
import it.unibo.`is`.interfaces.protocols.IConnInteraction  //WARNING !!! 
import lowLevelComms.tcpConnSupport
import highLevelComms.hlComm

var hlCommSupport : hlComm? =  null

fun lowLevelConnect( host:String, port:Int ) : IConnInteraction{
	val socket  = lowLevelComms.tecnoSupport.connectAsClient(host,port)
	return tcpConnSupport( socket )
}

fun clientStartUp(){
	val conn = lowLevelConnect("localhost", 8010 )
	hlCommSupport = hlComm( conn )
}

fun clientWork(){
	println("client | sending ... ")
	hlCommSupport!!.forward("sonar","polar","polar(40,0)","radargui")
}

fun main() {
    println("client | BEGIN")
	clientStartUp()
	clientWork()
    println("client | END")
}
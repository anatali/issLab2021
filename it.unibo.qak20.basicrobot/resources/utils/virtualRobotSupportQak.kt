package utils
//virtualRobotSupportQak.kt

import java.io.PrintWriter
import java.net.Socket
import org.json.JSONObject
import java.io.BufferedReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.InputStreamReader
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import utils.Messages
import it.unibo.kactor.ActorBasic
 
 //A support for using the virtual robot
 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
object virtualRobotSupportQak {
        private var hostName = "localhost"
        private var port     = 8999
        private val sep      = ";"
        private var outToServer : PrintWriter?     = null
        private lateinit var owner : ActorBasic
        private val applCmdset = setOf("w","s","a","d","z","x","r","l","h"  )
        private var sensorObserver : Job? = null
	
	
        var traceOn = false
	
	fun trace( msg: String ){
		if( traceOn )  println("			*** virtualRobotSupportQak | $msg")
	}

	fun initClientConn( owner: ActorBasic, hostNameStr: String = hostName, portStr: String = "$port"  ) {
            hostName         = hostNameStr
            port             = Integer.parseInt(portStr)
             try {
                 val clientSocket = Socket(hostName, port)
                 trace("CONNECTION DONE with $port")
                 outToServer  = PrintWriter(clientSocket.getOutputStream())
                 startSensorObserver( owner, clientSocket )
             }catch( e:Exception ){
                 println("			*** virtualRobotSupportQak | ERROR $e")
             }
	}

/*
 	Performs a move 
*/		
        fun domove(cmd: String) {	//cmd is written in cril 
            val jsonObject = JSONObject(cmd )
            val msg = "$sep${jsonObject.toString()}$sep"
            outToServer?.println(msg)
            outToServer?.flush()
        }
//translates application-language in cril
        fun translate(cmd: String) : String{ //cmd is written in application-language
		var jsonMsg = "{ 'type': 'alarm', 'arg': -1 }"
			when( cmd ){
				"msg(w)", "w" -> jsonMsg = "{ 'type': 'moveForward',  'arg': -1 }"
				"msg(s)", "s" -> jsonMsg = "{ 'type': 'moveBackward', 'arg': -1 }"
				"msg(a)", "a" -> jsonMsg = "{ 'type': 'turnLeft',  'arg': -1  }"
				"msg(d)", "d" -> jsonMsg = "{ 'type': 'turnRight', 'arg': -1  }"
				"msg(l)", "l" -> jsonMsg = "{ 'type': 'turnLeft',  'arg': 300 }"
				"msg(r)", "r" -> jsonMsg = "{ 'type': 'turnRight', 'arg': 300 }"
				"msg(z)", "z" -> jsonMsg = "{ 'type': 'turnLeft',  'arg': -1  }"
				"msg(x)", "x" -> jsonMsg = "{ 'type': 'turnRight', 'arg': -1  }"
				"msg(h)", "h" -> jsonMsg = "{ 'type': 'alarm',     'arg': 100 }"
				else -> println("virtualRobotSupportQak command $cmd unknown")
			}
            val jsonObject = JSONObject( jsonMsg )
            val msg = "$sep${jsonObject.toString()}$sep"
			return msg
		}
	
	    fun halt(){
            domove("{ 'type': 'alarm',     'arg': 100 }")
  		}
        suspend fun doApplMove(cmd: String) {	//cmd is written in application-language
			halt()	//defensive ...
			val msg = translate( cmd )
			trace("doApplMove  $msg")
            outToServer?.println(msg)
            outToServer?.flush()
			if( cmd=="l" || cmd =="r") { delay( 300 ) } 
        }
	

	
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
        private fun startSensorObserver( owner: ActorBasic, clientSocket : Socket ) {
		val inFromServer = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
		val scope : CoroutineScope = CoroutineScope( Dispatchers.Default )
	    sensorObserver = scope.launch {
 				println("			*** virtualRobotSupportQak | sensorObserver STARTS ")
                while (true) {
                    try {
                        val inpuStr = inFromServer.readLine()
                        val jsonMsgStr =
                            inpuStr!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                        //trace("inpuStr= $jsonMsgStr")
                        val jsonObject = JSONObject(jsonMsgStr)
                        //trace( "type: " + jsonObject.getString("type"))
                        when (jsonObject.getString("type")) {
                            "webpage-ready" -> trace("webpage-ready ")
                            "sonar-activated" -> {
                                val jsonArg   = jsonObject.getJSONObject("arg")
                                val sonarName = jsonArg.getString("sonarName")
                                val distance  = jsonArg.getInt("distance")							 
 								/*
 									Transforms the owner as a source of events
 								*/
								val m1 = "sonar($sonarName, $distance, avatar = true)"
								owner.emit("sonar",m1)
 								//Messages.emit( "vr", "sensor",  "sensor($sonarName,$distance)", owner  )
 
                            }
                            "collision" -> {
                                val jsonArg    = jsonObject.getJSONObject("arg")
                                val objectName = jsonArg.getString("objectName")
								//println("virtualRobotSupportQak collision $objectName")
								/*
 									Transforms the owner as a source of events
 								*/
								owner.emit("collision",  "collision( $objectName, avatar = true )")
 								//Messages.emit( "vr", "collision",  "collision( $objectName )", owner  )
                             }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
         }//startSensorObserver 

fun terminatevr(){
	if( sensorObserver!= null ) sensorObserver!!.cancel()
	trace("TERMINATES sensorObserver")
}

}

 








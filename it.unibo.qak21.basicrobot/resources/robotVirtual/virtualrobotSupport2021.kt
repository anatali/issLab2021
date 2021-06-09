package robotVirtual

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
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import it.unibo.supports.IssWsHttpKotlinSupport
import it.unibo.interaction.MsgRobotUtil

 //A support for using the virtual robot
 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
object virtualrobotSupport2021 {
	lateinit var owner      : ActorBasic
	lateinit var robotsonar	: ActorBasic
	    private var hostName = "localhost"
        private var port     = 8090
        private val sep      = ";"
        //private var outToServer : PrintWriter?     = null
	
        private lateinit var support21 : IssWsHttpKotlinSupport 
        private val applCmdset = setOf("w","s","a","d","z","x","r","l","h"  )

        var traceOn = true
	
	init{
		println(" CREATING")
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun create( owner: ActorBasic, hostNameStr: String, portStr: String, trace : Boolean = false  ){
 		this.owner   = owner	 
 		this.traceOn = trace
 		//initClientConn
            hostName         = hostNameStr
            port             = Integer.parseInt(portStr)
             try {
            	support21    = IssWsHttpKotlinSupport.createForHttp(owner.scope, "$hostNameStr:$portStr" )
            	println("		--- virtualrobotSupport2021 |  created ")	
				 
				 /*
				 val clientSocket = Socket(hostName, port)
                trace("CONNECTION DONE with $port")
                outToServer  = PrintWriter(clientSocket.getOutputStream())

				//ACTIVATE the robotsonar as the beginning of a pipe
				robotsonar = virtualrobotSonarSupportActor("robotsonar", clientSocket)
				owner.context!!.addInternalActor(robotsonar)

			  	println("		--- virtualrobotSupport2021 | has created the robotsonar")	
*/
			 }catch( e:Exception ){
                 println("			*** virtualrobotSupport2021 | ERROR $e")
             }	
	}
	
	fun trace( msg: String ){
		if( traceOn )  println("			*** virtualrobotSupport2021 trace | $msg")
	}



    fun move(cmd: String) {	//cmd is written in application-language
			val msg = translate( cmd )
			trace("move  $msg")
		/*
            outToServer?.println(msg)
            outToServer?.flush()
			if( cmd=="l" || cmd =="r") { Thread.sleep( 300 ) } */
			support21.sendHttp(msg,"192.168.1.64:8090/api/move")
    }
/*
 	Performs a move written in cril
		
        fun domove(cmd: String) {	//cmd is written in cril 
            val jsonObject = JSONObject(cmd )
            val msg = "$sep${jsonObject.toString()}$sep"
            outToServer?.println(msg)
            outToServer?.flush()
        }*/
//translates application-language in cril
        fun translate(cmd: String) : String{ //cmd is written in application-language
		var jsonMsg = MsgRobotUtil.haltMsg //"{ 'type': 'alarm', 'arg': -1 }"
			when( cmd ){
				"msg(w)", "w" -> jsonMsg = MsgRobotUtil.forwardMsg //"{ 'type': 'moveForward',  'arg': -1 }"
				"msg(s)", "s" -> jsonMsg = MsgRobotUtil.backwardMsg //"{ 'type': 'moveBackward', 'arg': -1 }"
				"msg(a)", "a" -> jsonMsg = MsgRobotUtil.turnLeftMsg //"{ 'type': 'turnLeft',  'arg': -1  }"
				"msg(d)", "d" -> jsonMsg = MsgRobotUtil.turnRightMsg //"{ 'type': 'turnRight', 'arg': -1  }"
				"msg(l)", "l" -> jsonMsg = MsgRobotUtil.turnLeftMsg // "{ 'type': 'turnLeft',  'arg': 300 }"
				"msg(r)", "r" -> jsonMsg = MsgRobotUtil.turnRightMsg //"{ 'type': 'turnRight', 'arg': 300 }"
				//"msg(z)", "z" -> jsonMsg = "{ 'type': 'turnLeft',  'arg': -1  }"
				//"msg(x)", "x" -> jsonMsg = "{ 'type': 'turnRight', 'arg': -1  }"
				"msg(h)", "h" -> jsonMsg = MsgRobotUtil.haltMsg //"{ 'type': 'alarm',     'arg': 100 }"
				else -> println("virtualrobotSupport2021 command $cmd unknown")
			}
            //val jsonObject = JSONObject( jsonMsg )
            //val msg = "$sep${jsonObject.toString()}$sep"
            //println("virtualrobotSupport2021 msg=$msg  ")
			return jsonMsg
		}
	
	    fun halt(){ move("h") } //domove("{ 'type': 'alarm',     'arg': 100 }") }
	

fun terminate(){
	robotsonar.terminate()
}	
	
 
 

}

 








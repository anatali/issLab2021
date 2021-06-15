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
	//private val applCmdset = setOf("w","s","a","d","z","x","r","l","h"  )
	private var port     = 0
	lateinit var owner      : ActorBasic
	lateinit var robotsonar	: ActorBasic
	private lateinit var hostName : String 	
	private lateinit var support21 : IssWsHttpKotlinSupport 

	var traceOn = false
	
	init{
		//println(" CREATING")
		IssWsHttpKotlinSupport.trace = false
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
				//ACTIVATE the robotsonar as the beginning of a pipe
				robotsonar = virtualrobotSonarSupportActor("robotsonar", null)
				owner.context!!.addInternalActor(robotsonar)  
			  	println("		--- virtualrobotSupport | has created the robotsonar")	
			 }catch( e:Exception ){
                 println("		--- virtualrobotSupport2021 | ERROR $e")
             }	
	}
	
	fun trace( msg: String ){
		if( traceOn )  println("		--- virtualrobotSupport2021 trace | $msg")
	}

    fun move(cmd: String) {	//cmd is written in application-language
		val msg = translate( cmd )
		trace("move  $msg")
		val answer = support21.sendHttp(msg,"$hostName:$port/api/move")
		trace("		--- virtualrobotSupport2021 | answer=$answer")
		//REMEMBER: answer={"endmove":"true","move":"alarm"} alarm means halt
		val ajson = JSONObject(answer)
		if( ajson.has("endmove") && ajson.get("endmove")=="false"){
			owner.scope.launch{  owner.emit("obstacle","obstacle(virtual)") }
		}
    }
    //translates application-language in cril
    fun translate(cmd: String) : String{ //cmd is written in application-language
		var jsonMsg = MsgRobotUtil.haltMsg //"{ 'type': 'alarm', 'arg': -1 }"
			when( cmd ){
				"msg(w)", "w" -> jsonMsg = MsgRobotUtil.forwardMsg  
				"msg(s)", "s" -> jsonMsg = MsgRobotUtil.backwardMsg  
				"msg(a)", "a" -> jsonMsg = MsgRobotUtil.turnLeftMsg  
				"msg(d)", "d" -> jsonMsg = MsgRobotUtil.turnRightMsg  
				"msg(l)", "l" -> jsonMsg = MsgRobotUtil.turnLeftMsg  
				"msg(r)", "r" -> jsonMsg = MsgRobotUtil.turnRightMsg  
				//"msg(z)", "z" -> not implemented
				//"msg(x)", "x" -> not implemented
				"msg(h)", "h" -> jsonMsg = MsgRobotUtil.haltMsg //"{ 'type': 'alarm',     'arg': 100 }"
				else -> println("		--- virtualrobotSupport2021 | command $cmd unknown")
			}
 			return jsonMsg
		}

	fun terminate(){
		robotsonar.terminate()
	}	
	
}//virtualrobotSupport2021

 








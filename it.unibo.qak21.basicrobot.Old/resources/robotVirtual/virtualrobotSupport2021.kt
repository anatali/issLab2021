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
	private var port     = 0
	lateinit var owner      : ActorBasic
	lateinit var robotsonar	: ActorBasic
	private lateinit var hostName : String 	
	private lateinit var support21 : IssWsHttpKotlinSupport 	//see project it.unibo.kotlinSupports
	private lateinit var support21ws : IssWsHttpKotlinSupport 	//see project it.unibo.kotlinSupports
	
	//WARNING: the virtualrobot REQUIRES that a move has a limited duration
	//We set here a duration long enough to assure the raising of a 'collision' in the working room
	//Moreover, we assume that the robot backwards for a shorter time
    private val forwardlongtimeMsg  = "{\"robotmove\":\"moveForward\",  \"time\": 2500}"
    private val backwardlongtimeMsg = "{\"robotmove\":\"moveBackward\", \"time\": 1000}"

	var traceOn = false
	
	init{
		println("virtualrobotSupport2021 | init ... ")
		IssWsHttpKotlinSupport.trace = false
	}
/*	
val doafterConn : (CoroutineScope, IssWsHttpKotlinSupport) -> Unit =
     fun(scope, support ) {
		val obs  = WsSupportObserver("wsSupportObs", scope, owner)
        println("virtualrobotSupport2021 | doafterConn REGISTER an observer for the IssWsHttpKotlinSupport")
		support.registerActor( obs )
}*/
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun create( owner: ActorBasic, hostNameStr: String, portStr: String, trace : Boolean = false  ){
 		this.owner   = owner	 
 		this.traceOn = trace
 		//initClientConn
            hostName         = hostNameStr
            port             = Integer.parseInt(portStr)
             try {
				IssWsHttpKotlinSupport.trace = true
            	support21    = IssWsHttpKotlinSupport.createForHttp(owner.scope, "$hostNameStr:$portStr" )
				support21ws  = IssWsHttpKotlinSupport.createForWs(owner.scope, "$hostNameStr:8091" )
            	println("		--- virtualrobotSupport2021 |  created (ws) $hostNameStr:$portStr $support21 $support21ws")	
				//support21ws.wsconnect( doafterConn )  //ALREADY DONE BY createForWs
            	val obs  = WsSupportObserver("wsSupportObs", owner.scope, owner)
              	support21ws.registerActor( obs )				 
				//support21ws.sendWs(MsgRobotUtil.turnLeftMsg)				  
				//support21ws.forward(MsgRobotUtil.turnRightMsg)
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
		//println("		--- virtualrobotSupport2021 |  moveeeeeeeeeeeeeeeeeeeeee $cmd ")
		val msg = translate( cmd ) 
		trace("move  $msg")
		if( cmd == "w" || cmd == "s"){  //doing aysnch
			//println("		--- virtualrobotSupport2021 |  wwwwwwwwwwwwwwwwwwwwwwwwww $support21ws")
			support21ws.sendWs(msg)	//aysnch => no immediate answer 
			return
		}
		val answer = support21.sendHttp(msg,"$hostName:$port/api/move")
		trace("		--- virtualrobotSupport2021 | answer=$answer")
		//REMEMBER: answer={"endmove":"true","move":"alarm"} alarm means halt
		try{
			val ajson = JSONObject(answer)
			if( ajson.has("endmove") && ajson.get("endmove")=="false" && cmd != "l" && cmd != "r" ){
				owner.scope.launch{  owner.emit("obstacle","obstacle($cmd)") }
			} 
		}catch(e: Exception){
			println("		--- virtualrobotSupport2021 |  move answer JSON ERROR $answer")
		}
    } 
    //translates application-language in cril
    fun translate(cmd: String) : String{ //cmd is written in application-language
		var jsonMsg = MsgRobotUtil.haltMsg //"{ 'type': 'alarm', 'arg': -1 }"
			when( cmd ){
				"msg(w)", "w" -> jsonMsg = forwardlongtimeMsg  		
				"msg(s)", "s" -> jsonMsg = backwardlongtimeMsg  
				"msg(a)", "a" -> jsonMsg = MsgRobotUtil.turnLeftMsg  
				"msg(d)", "d" -> jsonMsg = MsgRobotUtil.turnRightMsg  
				"msg(l)", "l" -> jsonMsg = MsgRobotUtil.turnLeftMsg  
				"msg(r)", "r" -> jsonMsg = MsgRobotUtil.turnRightMsg  
				//"msg(z)", "z" -> not implemented
				//"msg(x)", "x" -> not implemented
				"msg(h)", "h" -> jsonMsg = MsgRobotUtil.haltMsg //"{ 'type': 'alarm','arg': 100 }"
				else -> println("		--- virtualrobotSupport2021 | command $cmd unknown")
			}
 			return jsonMsg
		}
	fun terminate(){
		robotsonar.terminate()
	}	
	
}//virtualrobotSupport2021

 








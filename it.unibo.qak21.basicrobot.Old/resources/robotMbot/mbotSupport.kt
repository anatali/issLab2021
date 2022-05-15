package robotMbot
/*
 -------------------------------------------------------------------------------------------------
 A factory that creates the support for the mbot
 "/dev/ttyUSB0"
 -------------------------------------------------------------------------------------------------
 */

import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ActorBasicFsm
import it.unibo.kactor.MsgUtil
//import it.unibo.supports.serial.SerialPortConnSupport
//import it.unibo.supports.serial.JSSCSerialComm


object mbotSupport{
	lateinit var owner   : ActorBasic
 	var conn             : SerialPortConnSupport? = null
	var dataSonar        : Int = 0 ; //Double = 0.0
 			
	//Called by robotSupport
	fun create( owner: ActorBasic, port : String ="/dev/ttyUSB0"  ) : SerialPortConnSupport?{
		this.owner = owner	//
		//initConn 
 		try {
 
			//println("   	%%% mbotSupport | initConn starts port=$port")
 			val serialConn = JSSCSerialComm()
			//val serialConn = JSSCSerialComm(null)
			conn = serialConn.connect(port)	//returns a SerialPortConnSupport
			println("   	%%% mbotSupport |  initConn port=$port conn= $conn")
			return conn
		}catch(  e : Exception) {
			println("   	%%% mbotSupport |  ERROR ${e }"   );
			return null;
		}		
	}
	
 
	
	/*
 	 Aug 2019
     The moves l, r, z, x can be executed  
 	  by the Python application robotCmdExec that exploits GY521
    */
	fun  move( cmd : String ){
		//println("  	%%% mbotSupport | move cmd=$cmd conn=$conn")
		if( conn != null ) {
			when( cmd ){
				"msg(w)", "w" -> conn!!.sendALine("w")
				"msg(s)", "s" -> conn!!.sendALine("s")
				"msg(a)", "a" -> conn!!.sendALine("a")
				"msg(d)", "d" -> conn!!.sendALine("d")
				"msg(l)", "l" -> conn!!.sendALine("l")  
				"msg(r)", "r" -> conn!!.sendALine("r")  
				"msg(z)", "z" -> conn!!.sendALine("z")  
				"msg(x)", "x" -> conn!!.sendALine("x")  
				"msg(h)", "h" -> conn!!.sendALine("h")
				else -> println("   	%%% mbotSupport | command $cmd unknown")
			}
		}
		 
	}
	
	private fun sendToPython( msg : String ){
		println("mbotSupport sendToPython $msg")
		owner.scope.launch{ owner.emit("rotationCmd","rotationCmd($msg)") }
	}
	
 }
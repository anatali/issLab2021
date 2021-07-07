package unibo.robot
/*
 -------------------------------------------------------------------------------------------------
 A factory that creates the proper support for each specific robot type
 
 NOV 2019:
 The operation create accept as last argument (filter) an ActorBasic to be used
 as a data-stream handler
 
 The operation subscribeToFilter subscribes to the given data-stream handler
 (dsh) another ActorBasic that should work as a data-stream handler
 -------------------------------------------------------------------------------------------------
 */

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ActorBasicFsm
import org.json.JSONObject
import java.io.File
import it.unibo.kactor.MsgUtil
import robotMbot.robotDataSourceArduino

//For testcrono
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
object robotSupport{
	lateinit var robotKind  :  String
	var endPipehandler      :  ActorBasic? = null 
	
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun testCrono(owner: ActorBasic){
		println( "		--- robotSupport | testcronooooooooooooooooooooooooooooooooooooooooo" )
		//Runtime.getRuntime().exec("sudo ./linuxCrono") //ADDED June 21 to check the time measurement
		//val cronotester = robotNano.cronoViewerActor("cronotester" )
		//owner.context!!.addInternalActor(cronotester)
		    try{
				val p      = Runtime.getRuntime().exec("sudo ./cronoLinux")
				val reader = BufferedReader(  InputStreamReader(p.getInputStream() ))
		println(" startReaddddddddddddddddddddddddddddddd ")
 		GlobalScope.launch{	 
		while( true ){
				var data = reader.readLine()
				println("crono data = $data"   )
			if( data == null ) break 
 		}
		}
			}catch( e : Exception){
				println(" WARNING:  does not find sleepcrono")
			}

	}
	
	fun create( owner: ActorBasic, configFileName: String, endPipe: ActorBasic? = null ){
		endPipehandler      =  endPipe
		
		
		//read Confif.json file
		val config = File("${configFileName}").readText(Charsets.UTF_8)
		val jsonObject   = JSONObject( config )
		val hostAddr     = jsonObject.getString("ipvirtualrobot") 
		robotKind        = jsonObject.getString("type") 
		val robotPort    = jsonObject.getString("port") 
		println( "		--- robotSupport | CREATED for $robotKind host=$hostAddr port=$robotPort owner=$owner" )

		when( robotKind ){		
			//"mockrobot"  ->  { robotMock.mockrobotSupport.create(  ) }
			"virtual"    ->  { robotVirtual.virtualrobotSupport2021.create( owner, hostAddr, robotPort) }
			"realmbot"   ->  {
				//testCrono(owner)
				
				val conn      = robotMbot.mbotSupport.create( owner, robotPort  ) //robotPort="/dev/ttyUSB0"   "COM6"
				if( conn != null ){
					val realsonar = robotDataSourceArduino("realsonar", owner,   conn )
					//Context injection  
					owner.context!!.addInternalActor(realsonar)  
                    println("		--- robotSupport | has created the realsonar for mobotttttttttt")
				}		
			} 
 			"realnano"   ->  {
 				robotNano.nanoSupport.create( owner )
 				val realsonar = robotNano.sonarHCSR04SupportActor("realsonar for nano")
				//Context injection  
				owner.context!!.addInternalActor(realsonar)  
  				println("		--- robotSupport | has created the realsonar")
			}
 			else -> println( "		--- robotSupport | robot $robotKind unknown" )
 		}
	}
	
	fun subscribe( obj : ActorBasic ) : ActorBasic {
		if( endPipehandler != null ) endPipehandler!!.subscribe( obj )
		return obj
	}
	 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	fun move( cmd : String ){ //cmd = w | a | s | d | h
 		//println("robotSupport move cmd=$cmd robotKind=$robotKind" ) 
		when( robotKind ){
			//"mockrobot"  -> { robotMock.mockrobotSupport.move( cmd ) 					  }
			"virtual"    -> { robotVirtual.virtualrobotSupport2021.move(  cmd ) 	  }
			"realmbot"   -> { robotMbot.mbotSupport.move( cmd ) 	}
 			"realnano"   -> { robotNano.nanoSupport.move( cmd)	}
			else         -> println( "		--- robotSupport | robot unknown")
		}		
	}
	
	fun terminate(){
		when( robotKind ){
			"mockrobot"  -> {  					                  }
			"virtual"    -> { robotVirtual.virtualrobotSupport2021.terminate(  ) 	  }
 			"realmbot"   -> { /* robotMbot.mbotSupport.terminate(  ) */	}
 			"realnano"   -> { robotNano.nanoSupport.terminate( )	}
			else         -> println( "		--- robotSupport | robot unknown")
		}		
		
	}
}
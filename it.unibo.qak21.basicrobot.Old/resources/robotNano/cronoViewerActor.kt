package robotNano

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage

/* 
 Emits the event sonarRobot : sonar( V )
 */
class cronoViewerActor ( name : String ) : ActorBasic( name ) {
	lateinit var reader : BufferedReader
	 
	init{
		println("$tt $name | CREATING")
		 
	}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg : ApplMessage){
 		println("$tt $name | received  $msg "  )
		if( msg.msgId() == "cronoViewerstart"){
			println("$tt $name | STARTING")
			try{
				val p = Runtime.getRuntime().exec("sudo ./linuxCrono")
				reader = BufferedReader(  InputStreamReader(p.getInputStream() ))
				startRead(   )
			}catch( e : Exception){
				println("$tt $name | WARNING:  does not find linuxCrono")
			}
 		}
     }
		
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun startRead(   ){
 		GlobalScope.launch{	//to allow message handling
		while( true ){
				var data = reader.readLine()
				println("$tt $name |  data = $data"   )
 		}
		}
	}
}
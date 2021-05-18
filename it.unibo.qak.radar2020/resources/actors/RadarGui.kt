package actors

import it.unibo.kactor.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import alice.tuprolog.Term
import alice.tuprolog.Struct
import kotlinx.coroutines.CoroutineScope

var radar : ActorBasic? = null
var sonar : ActorBasic? = null
var other : ActorBasic? = null


class RadarGui( name : String, scope : CoroutineScope, confined : Boolean = false ) :
			ActorBasic( name, scope, confined ){

	init{
		radarPojo.radarSupport.setUpRadarGui()
	}
	
	override suspend fun actorBody(msg : ApplMessage){ //msg-driven		
		println( msg )		
		val msgContent = msg.msgContent() //polar(D,A)
		val tt = Term.createTerm( msgContent ) as Struct
		val DistanceStr = tt.getArg(0).toString()
		val AngleStr    = tt.getArg(1).toString()
		println( DistanceStr )	
 		radarPojo.radarSupport.update( DistanceStr, AngleStr)
	}
}

class SonarSimulator( name : String, scope : CoroutineScope, confined : Boolean = false  ) :
	ActorBasic( name, scope, confined ){
	
	override suspend fun actorBody(msg : ApplMessage){ //msg-driven
		println( msg )
		work()
	}
	
/*
 sonarDataSimulator.getSonarValBlocking() blocks the current Thread on a readLine
 Thus it could block also other kotlin actors (coroutines) working in the same Thread
 a delay could allow some system behavior, BUT IS IS WRONG TO DO SO
 
 sonarDataSimulator.getSonarValBlocking() can be used if confined = false
 but REMEMBER: ActorBasic starts as many Threads as the available CPUs
 */
	suspend fun work(){
		while( true ){
			//val v = utils.sonarDataSimulator.getSonarValBlocking()  
			val v = utils.sonarDataSimulator.getSonarVal()
			println( "SonarSimulator forwards $v" )
			forward( "polar", "polar($v,0)", radar!!)
			//delay( 2000 )  //WORKS, but IS THE WRONG WAY ...
		}
	}
}

class DummyActor( name : String, scope : CoroutineScope, confined : Boolean = false  )
	: ActorBasic( name, scope, confined ){
	
	override suspend fun actorBody(msg : ApplMessage){ //msg-driven
		println( msg )
		work()
	}
	
	suspend fun work(){
		while( true ){
			println("dummy is working ... " )
			delay( 2000 )
		}
	}
}

val cpus = Runtime.getRuntime().availableProcessors();

fun curThread() : String { 
	return "thread=${Thread.currentThread().name} / nthreads=${Thread.activeCount()}" 
}
 
fun main() = runBlocking {    
    println("RadarGui | START  CPU=$cpus ${curThread()}")
	radar = RadarGui("radargui", this, true)	         //WARNING: confined=true means 1 Thread
	sonar = SonarSimulator("sonarsimulator", this, true) //WARNING: confined=true means 1 Thread
	other = DummyActor("other", this, true)              //WARNING: confined=true means 1 Thread
	MsgUtil.sendMsg( "main", "start", "start", sonar!!)
	MsgUtil.sendMsg( "main", "start", "start", other!!)
 }

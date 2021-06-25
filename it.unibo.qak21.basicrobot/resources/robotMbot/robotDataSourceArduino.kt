package robotMbot
/*
 -------------------------------------------------------------------------------------------------
 Reads data from the serial connection to Arduino
 For each data value V, it emitLocalStreamEvent sonarRobot:sonar(V)
 -------------------------------------------------------------------------------------------------
 */
import it.unibo.kactor.ActorBasicFsm
import kotlinx.coroutines.launch
import java.io.BufferedReader
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Term
import alice.tuprolog.Struct
//import it.unibo.supports.serial.SerialPortConnSupport


@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class  robotDataSourceArduino( name : String, val owner : ActorBasic ,
					val conn : SerialPortConnSupport) : ActorBasic(name, owner.scope){		
	init{
		scope.launch{  autoMsg("start","start(1)") }
		//println("   	%%% $name |  starts conn=$conn")	 
	}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	override suspend fun actorBody(msg: ApplMessage) {
        println("   	%%% $name |  handles msg= $msg conn=$conn owner=$owner")
		//val vStr  = (Term.createTerm( msg.msgContent()) as Struct).getArg(0).toString()
		//println("   	%%% $name |  handles msg= $msg  vStr=$vStr")
		readData(   )
	}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun readData(  ){
         while ( true ) {
 			try {
 				var curDataFromArduino =  conn.receiveALine()
				//globalTimer.startTimer()  //TIMER ....
 	 			//println("   	%%% $name | readData: $curDataFromArduino"    )
				if( curDataFromArduino != null ){
	 				var v = curDataFromArduino.toDouble() 
					//handle too fast change ?? NOT HERE
	  				var dataSonar = v.toInt();													
	 				//if( dataSonar < 350 ){ /REMOVED since USING STREAMS
	 				val event = MsgUtil.buildEvent( name,"sonarRobot","sonar( $dataSonar )")								
	  				//println("   	%%% $name | mbotSupport event: ${ event } owner=${owner.name}"   );						
					emitLocalStreamEvent( event )
				}
 				//Oct2019 : emit the event obstacle
/* //REMOVED WHEN USING STREAMS							
				if( dataSonar < 7  ){ //WARNING: it generates  many events
				if( ! obstacleEventEmitted ){ //Math.abs(dataSonar - oldSonarValue) > 3
				//println("   	%%% $name | mbotSupport sonar: ${ dataSonar } r"   );
				val obstacle = MsgUtil.buildEvent( name,"obstacle","obstacle($dataSonar)")
	 			owner.emit(  obstacle )
				obstacleEventEmitted = true
				}else obstacleEventEmitted = false												
 */
			} catch ( e : Exception) {
 				//println("   	%%% $name | getDataFromArduino | ERROR $e   ")
            }
		}//while
	}
	
 
}
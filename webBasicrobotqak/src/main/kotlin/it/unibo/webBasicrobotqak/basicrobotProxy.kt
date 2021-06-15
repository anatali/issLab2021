package it.unibo.webBasicrobotqak

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import kotlinx.coroutines.channels.Channel

/*
This qakactor is part of the basicrobot system.
Its goal is to work as an observer
 */
class basicrobotProxy (name : String ) : ActorBasic( name ) {
	var answerMoveChannel = Channel<String>()
	//val resource = ResourceRep.getResourceRep()
init{
	//sysUtil.colorPrint("basicrobotProxy | ${HIController.answerMoveChannel}  ${answerChannel}", Color.GREEN)
	//answerMoveChannel = HIController.getAnswerChannel()
	//sysUtil.colorPrint("basicrobotProxy | resource ${resource.robotanswerChannel} scope=$scope", Color.GREEN)

	//HIController.setAnswerChannel(answerMoveChannel)
}
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg: ApplMessage) {
  		elabData( msg )
 	}

 	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	  suspend fun elabData( msg: ApplMessage ){
	      sysUtil.colorPrint("basicrobotProxy | elabData:${msg} ", Color.BLUE)
	/*
		  if( msg.isReply() ) {
			  //sysUtil.colorPrint("basicrobotProxy | elabData:${msg} ${answerMoveChannel}", Color.BLUE)
			  //answerMoveChannel.send( msg.toString() )
			  //answerChannel.put( msg.toString() )
			  sysUtil.colorPrint("basicrobotProxy | elabDataDONE ", Color.BLUE)
		  }*/
	  }
}
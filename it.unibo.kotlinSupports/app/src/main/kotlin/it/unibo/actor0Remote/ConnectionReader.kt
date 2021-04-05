package it.unibo.actor0Remote

import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.DispatchType
import it.unibo.actor0.sysUtil
import kotlinx.coroutines.*

class ConnectionReader (name: String, val conn: IConnInteraction ) :
    ActorBasicKotlin( name ) { //DispatchType.single, DispatchType.iobound


    suspend fun getInput( obj : ConnectionReader) {
            while (true) {
                //println("$name  | getInput scope=$scope dispatcher=$dispatcher  ${infoThreads()}")
                val v = conn.receiveALine()
                println("$name | RECEIVES: $v")
                if (v != null) {
                    val msg = ApplMessage.create(v)
                    obj.updateObservers(msg)
                } else{

                    break
                }
            }//while
        println("$name  | TERMINATE -------------------------- ")
            terminate()
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println( "$name  | $msg ${infoThreads()}" )
        if(msg.msgId == "start"){
             getInput( this )  //blocked ...
         }
    }
}
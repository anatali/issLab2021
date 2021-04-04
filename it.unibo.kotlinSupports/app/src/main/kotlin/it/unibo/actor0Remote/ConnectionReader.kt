package it.unibo.actor0Remote

import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.DispatchType
import kotlinx.coroutines.GlobalScope

class ConnectionReader (name: String, val conn: IConnInteraction) :
    ActorBasicKotlin(name, GlobalScope, DispatchType.iobound) {
    var goon = true

    suspend fun getInput() {
        while (goon) {
            //println( "$name  | getInput $goon ${infoThreads()}" )
            val v = conn.receiveALine()
            println("$name | RECEIVES: $v")
            val msg = ApplMessage.create(v)
            this.updateObservers(msg)
        }
        terminate()
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println( "$name  | $msg ${infoThreads()}" )
        if(msg.msgId == "start"){
             getInput()
         }else if(msg.msgId == "end"){
            goon = false
        }
    }
}


/*
============================================================
ConnectionReader

============================================================
 */
package it.unibo.actor0

import it.unibo.`is`.interfaces.protocols.IConnInteraction


class ConnectionReader (name: String, val conn: IConnInteraction ) :
    ActorBasicKotlin( name ) {


    suspend fun getInput( obj : ConnectionReader) {
            while (true) {
                //println("$name  | getInput scope=$scope dispatcher=$dispatcher  ${infoThreads()}")
                val v = conn.receiveALine()
                //println("$name | RECEIVES: $v")
                if (v != null) {
                    val msg = ApplMessage.create(v)
                    obj.updateObservers(msg)
                } else{

                    break
                }
            }//while
            //println("$name  | TERMINATE -------------------------- ")
            terminate()
    }

    override suspend fun handleInput(msg: ApplMessage) {
        //println( "$name  | $msg ${infoThreads()}" )
        if(msg.msgId == "start"){
             getInput( this )  //blocked ...
         }
    }
}
package it.unibo.supports
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.DispatchType
import it.unibo.actor0.MsgUtil
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject
import kotlinx.coroutines.delay

class TimerActor(name: String, owner: ActorBasicKotlin, scope: CoroutineScope)
                          : ActorBasicKotlin(name, scope, DispatchType.single) {
    //private long tout          = 0;
    private var owner: ActorBasicKotlin
    private var killed = false

    init {
        this.owner = owner
        println( "$name  | TimerActor init ${infoThreads()}")
    }

    override suspend fun handleInput(msg: ApplMessage) {
        val msgJson = JSONObject( msg.msgContent )
        if (msgJson.has("startTimer")) {
            val tout = msgJson.getString("startTimer").toLong()
            delay( tout )
            //println("$name | elapsed $tout msecs killed=$killed")
            if (owner != null && !killed) {
                //owner.send(ActorMsgs.endTimerMsg)
                val answerMsg =
                        MsgUtil.buildDispatch(name, ActorMsgs.endTimerId, ActorMsgs.endTimerMsg, owner.name )
                owner.send(answerMsg)
                terminate()
                //MsgUtil.sendMsg(name, ActorMsgs.endTimerId, ActorMsgs.endTimerMsg, owner  )
            } //
        }
    }

    fun kill() {
        //System.out.println( myname + " | kill " );
        terminate()
        killed = true
    }


}
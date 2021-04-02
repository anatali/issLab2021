package it.unibo.supports
import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.interaction.IJavaActor
import org.json.JSONObject
import kotlinx.coroutines.delay

class TimerActor(name: String, owner: IJavaActor) : ActorBasicKotlin(name) {
    //private long tout          = 0;
    private var owner: IJavaActor
    private var killed = false

    init {
        this.owner = owner
    }

    override suspend fun handleInput(msg: ApplMessage) {
        val msgJson = JSONObject( msg.msgContent )
        if (msgJson.has("startTimer")) {
            val tout = msgJson.getString("startTimer").toLong()
            delay( tout )

            println("$name | elapsed $tout msecs killed=$killed")
            if (owner != null && !killed) {
                //owner.send(ActorMsgs.endTimerMsg)
                MsgUtil.sendMsg(name, ActorMsgs.endTimerId, ActorMsgs.endTimerMsg, owner  )
            } //
        }
    }

    fun kill() {
        //System.out.println( myname + " | kill " );
        terminate()
        killed = true
    }


}
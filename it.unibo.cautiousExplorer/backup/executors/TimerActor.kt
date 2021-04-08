package executors
import it.unibo.actor0.*
import it.unibo.actor0Usage.ActorBasicKotlinNaive
import it.unibo.supports.ActorMsgs
import it.unibo.supports.TimerActor
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

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

fun main() {
    println("==============================================")
    println("Timer | START ${sysUtil.aboutSystem("timer") }"  );
    println("==============================================")
    lateinit var timer : TimerActor
    lateinit var obs : ActorBasicKotlinNaive
    runBlocking{
        obs   = ActorBasicKotlinNaive("obs", this )
        timer = TimerActor("t0", obs, this )
        val m = MsgUtil.buildDispatch("main","startTimer","{\"startTimer\":\"1000\" }", "to")
        timer.send(m)
        delay(1500) //change to some time > 1000 in order to update the observer
        timer.kill()
        delay(1500)
        obs.terminate()
    }
    //obs.terminate()
    println("==============================================")
    println("Timer | END  ${sysUtil.aboutThreads("applmain") }"  );
    println("==============================================")
}
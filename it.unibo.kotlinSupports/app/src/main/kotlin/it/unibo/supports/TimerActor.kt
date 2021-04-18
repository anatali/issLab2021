package it.unibo.supports
import com.andreapivetta.kolor.Color
import it.unibo.actor0.*
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class TimerActor(name: String, owner: ActorBasicKotlin, scope: CoroutineScope)
                          : ActorBasicKotlin(name, scope, DispatchType.single) {
    private var owner: ActorBasicKotlin
    private var killed = false

    init {
        this.owner = owner
        colorPrint( "$name  | TimerActor init ${infoThreads()}", Color.LIGHT_GRAY )
    }

    override suspend fun handleInput(msg: ApplMessage) {
        val msgJson = JSONObject( msg.msgContent )
        if (msgJson.has("startTimer")) {
            val tout = msgJson.getString("startTimer").toLong()
            delay( tout )
            colorPrint("$name | elapsed $tout msecs killed=$killed", Color.LIGHT_GRAY)
            if ( !killed ) {
                val answerMsg =
                        MsgUtil.buildDispatch(name, ActorMsgs.endTimerId, ActorMsgs.endTimerMsg, owner.name )
                owner.send(answerMsg)
                terminate()
            }
        }
    }

    fun kill() {
        colorPrint( "$name | kill ", Color.LIGHT_GRAY );
        terminate()
        killed = true
    }

}
@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
fun main() {
    println("==============================================")
    println("Timer | START ${sysUtil.aboutSystem("timer") }"  );
    println("==============================================")
    lateinit var timer : TimerActor
    lateinit var obs : ActorBasicKotlinNaive
    runBlocking{
        obs   = ActorBasicKotlinNaive("obs", this )
        timer = TimerActor("t0", obs, this )
        val m = MsgUtil.buildDispatch("main","startTimer","{\"startTimer\":\"300\" }", "to")
        timer.send(m)
        delay(1000)
        obs.terminate()
    }
    //obs.terminate()
    println("==============================================")
    println("Timer | END  ${sysUtil.aboutThreads("applmain") }"  );
    println("==============================================")
}
package it.unibo.executors


import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil
import it.unibo.supports.IssWsHttpKotlinSupport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mapRoomKotlin.mapUtil
import org.json.JSONObject

class Walker( name:String, scope: CoroutineScope) : AbstractRobotActor(name, scope) {

    protected enum class State {
        start, checkResultAhead, checkResultBack, end
    }

    protected var curState  =  State.start
    protected var pathTodo  =  ""

    init {
        support = IssWsHttpKotlinSupport.getConnectionWs(scope, "localhost:8091")
        println( "$name | Walker init ${infoThreads()}")
    }
    protected suspend fun fsm(move: String, arg: String) {
        println(name + " | state=" + curState + " move=" + move + " arg=" + arg)
        when (curState) {
            State.start -> {
                if( move=="start") {
                    curState = State.checkResultAhead
                    doPath(arg)
                }
            }
            State.checkResultAhead -> {
                if( move==ApplMsgs.executorDoneId  ){
                    turn180()
                    doPathBack( )
                    curState = State.checkResultBack
                }else{
                    println("$name | partial path=$arg")
                }
            }
            State.checkResultBack -> {
                println( "$name | All OK - BYE ${infoThreads()}")
                terminate()
            }
            State.end -> {

            }
        }
    }
    fun doPath(path : String){
        pathTodo = path
        println( "$name  | pathTodo: $pathTodo"  )
        val executorName = "execahaed"
        val executor = ExecutorActor(executorName, this, scope )
        val pathtodo = ApplMsgs.executorstartMsg.replace("PATHTODO",path)
        val startmsg = MsgUtil.buildDispatch(name, ApplMsgs.executorStartId, pathtodo, executorName )
        executor.send(startmsg)
    }

    fun doPathBack( ){
        val path = reversePath( pathTodo )+ "ll"
        println( "$name  | doPathBack: $path"  )
        val executorName = "execback"
        val executor = ExecutorActor(executorName, this, scope )
        val pathtodo = ApplMsgs.executorstartMsg.replace("PATHTODO",path)
        val startmsg = MsgUtil.buildDispatch(name, ApplMsgs.executorStartId, pathtodo, executorName )
        executor.send(startmsg)
    }

    protected fun reversePath(s: String): String {
        return if (s.length <= 1) s else reversePath(s.substring(1)) + s[0]
    }
    protected suspend fun turn180() {
            turnLeft()
            mapUtil.doMove("l")
            delay(300)
            turnLeft()
            mapUtil.doMove("l")
            delay(300)
    }

    override suspend fun handleInput(msg: ApplMessage) {
        println("$name  | handleInput $msg ${sysUtil.aboutThreads(name)}");
        if( msg.msgId == "start" ) fsm( msg.msgId,msg.msgContent )
            //doPath( msg.msgContent )
        else if( msg.msgId == ApplMsgs.executorDoneId ){
            println("$name  | Done with success")
            fsm( msg.msgId,msg.msgContent )
        }else if( msg.msgId == ApplMsgs.executorFailId){
            println("$name  | Failed with path done= ${msg.msgContent}")
            fsm( msg.msgId,msg.msgContent )
        }
    }

    override fun msgDriven(infoJson: JSONObject) {
        TODO("Not yet implemented")
    }
}
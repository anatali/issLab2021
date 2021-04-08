/*
============================================================
MainStepRobotActorCaller
Works as the Stepper, by sending commands over TCP-8010
============================================================
 */

package actor0Remote

import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.actor0.*
import it.unibo.actor0robot.ApplMsgs
import kotlinx.coroutines.*
import org.json.JSONObject

class StepRobotCaller(name: String, scope: CoroutineScope) : //, disp: DispatchType
    ActorBasicKotlin(name, scope) { //,disp default=DispatchType.single

    val factoryProtocol = MsgUtil.getFactoryProtocol(Protocol.TCP)
    lateinit var conn: IConnInteraction
    lateinit var reader: ConnectionReader
    val stepCmd = ApplMsgs.stepMsg.replace("TIME", "350")

    fun startConn() {
        conn = factoryProtocol.createClientProtocolSupport("localhost", ActorBasicContextKb.portNum)
        println("$name  | startConn $conn  ")
        reader = ConnectionReader("reader", conn)
        reader.registerActor(this)  //if we want to handle the answer from
        reader.send(MsgUtil.startDefaultMsg)
    }

    fun doStep(){
        //println("$name  | doStep  ${infoThreads()}")
        val msg = MsgUtil.buildDispatch(name, ApplMsgs.stepId, stepCmd, "stepRobot").toString()
        conn.sendALine(msg)
    }
    override suspend fun handleInput(msg: ApplMessage) {
        //println("$name  | $msg ${infoThreads()}")
        if (msg.msgId == "start") {
            startConn()
            doStep()
        }else if( msg.msgId == "stepAnswer" ){
            //println("$name  | $msg ${infoThreads()}")
            val answer = JSONObject(msg.msgContent)
            if( answer.has("stepDone")) doStep()
            else{
                println("$name  | handleInput $answer  ");
                terminate()
            }
        } else if (msg.msgId == "end") {
            //conn.closeConnection()
            terminate()  //also the connection and the reader ...
        }
    }

}


    @ExperimentalCoroutinesApi
    fun main() {
        //sysUtil.trace = true
        val startTime = sysUtil.aboutSystem("main")
        println("==============================================")
        println("MainStepRobotActorCaller | START ${sysUtil.aboutSystem("mainCtxServer")}");
        println("==============================================")

        runBlocking {
            val caller = StepRobotCaller("stepcaller", this)
            caller.send(MsgUtil.startDefaultMsg)
        }//runBlocking

        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainStepRobotActorCaller | END TIME=$endTime ${sysUtil.aboutThreads("main")}");
        println("==============================================")

    }


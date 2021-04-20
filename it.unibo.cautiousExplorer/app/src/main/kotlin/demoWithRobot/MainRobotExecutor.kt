/*
MainRobotExecutor

Application that uses the user-defined RobotExecutor
and the system-defined NaiveActorKotlinObserver
to perform a planned sequence of moves (PATHTODO)
 */
package demoWithRobot


import it.unibo.actor0.MsgUtil
import it.unibo.actor0.sysUtil.curThread
import kotlinx.coroutines.runBlocking
import it.unibo.actor0.sysUtil.cpus
import it.unibo.robotService.ApplMsgs
import kotlinx.coroutines.delay


    fun main( ) {
        println("BEGINS CPU=$cpus ${curThread()}")
        runBlocking {
            val obs      = NaiveObserverActorKotlin("obs",  this)
            val executor = RobotExecutor("executor", obs)
            val cmdStr   = ApplMsgs.executorstartMsg.replace("PATHTODO", "wl")
            val cmd      = MsgUtil.buildDispatch("main", ApplMsgs.executorStartId, cmdStr, "executor")
            executor.send(cmd)
            println("ENDS runBlocking ${curThread()}")
        }
        println("ENDS main ${curThread()}")
    }



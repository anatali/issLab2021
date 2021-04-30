package it.unibo.webspring.demo
import it.unibo.actor0.sysUtil
import it.unibo.robotService.BasicStepRobotActor
import kotlinx.coroutines.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@kotlinx.coroutines.ObsoleteCoroutinesApi
@RestController
class M2MRestController {
    val myscope = CoroutineScope(Dispatchers.Default)
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    lateinit var robot : BasicStepRobotActor
    init{
        //RobotResource.initRobotResource() //OLD APPROACH: we want a local BasicStepRobotActorCaller
        //val obs     = ObserverForSendingAnswer("obs", myscope)
        //robot       = BasicStepRobotActor("stepRobot", ownerActor= obs, myscope, "wenv")
    }
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    @PostMapping( "/moverest" )
    fun doMove( @RequestParam(name="move", required=false, defaultValue="h") moveName : String) : String {
        sysUtil.colorPrint("M2MRestController | $moveName")
        //RobotResource.execMove(moveName)    //OLD APPROACH
        //TODO: handle the answer to the move execution
        var result = "todo"
        val answer = runBlocking{
            val obs     = ObserverForSendingAnswer("obsforanswer", myscope)
            robot       = BasicStepRobotActor("stepRobot", ownerActor= obs, myscope, "wenv")
            //RobotResource.execMove(robot, moveName)

            val res : Deferred<String> = myscope.async{
                println("async starts")
                RobotResource.execMove(robot, moveName) //blokcs ...
                "robot has executed"
            }
            val r = res.await()
            result = obs.result
            println("async $r - $result")
            robot.terminate()
            obs.terminate()
         }
        return "done robotMOve: $result"

    }
    @PostMapping( "/dopath" )
    fun doPath( @RequestParam(name="path", required=false, defaultValue="") pathTodo : String) : String {
        sysUtil.colorPrint("M2MRestController | $pathTodo")
        //Activate PathExecutor and send the answer to the caller
        return "done $pathTodo"
    }
}
//curl -d move=l localhost:8081/moverest

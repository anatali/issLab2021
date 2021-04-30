package it.unibo.webspring.demo
import com.andreapivetta.kolor.Color
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
    lateinit var obs   : ObserverForSendingAnswer


    init{
        //RobotResource.initRobotResource() //OLD APPROACH: we want a local BasicStepRobotActorCaller
        obs     = ObserverForSendingAnswer("obsforanswer", myscope)
        robot   = BasicStepRobotActor("stepRobot", ownerActor= obs, myscope, "wenv")
    }
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    @PostMapping( "/moverest" )
    fun doMove( @RequestParam(name="move", required=false, defaultValue="h") moveName : String) : String {
        sysUtil.colorPrint("M2MRestController | $moveName | $this")
        //handle the answer to the move execution
        var result = "todo"
        runBlocking{
            //robot       = BasicStepRobotActor("stepRobot", ownerActor= obs, myscope, "wenv")
            var job = myscope.launch{
                RobotResource.execMove(robot, moveName)
                delay(500)  //TODO synch with obs
                result = obs.result
                sysUtil.colorPrint("obs result=$result  ")
                obs.result = "none"
            }//launch
            delay(600)
        }//runBlocking
        sysUtil.colorPrint("result=$result"  , Color.MAGENTA)
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
/*
            val res : Deferred<String> = myscope.async{
                println("async starts")
                RobotResource.execMove(robot, moveName) //blokcs ...
                "robot has executed"
            }
 */
//val r = res.await()

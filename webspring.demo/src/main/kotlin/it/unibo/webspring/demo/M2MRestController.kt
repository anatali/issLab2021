package it.unibo.webspring.demo

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import it.unibo.actorAppl.BasicStepRobotActorCaller
import it.unibo.robotService.ApplMsgs
import it.unibo.robotService.BasicStepRobotActor
import it.unibo.supports.NaiveActorKotlinObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class M2MRestController {

    init{
        RobotResource.initRobotResource()
    }
    @PostMapping( "/moverest" )
    fun doMove( @RequestParam(name="move", required=false, defaultValue="h") moveName : String) : String {
        sysUtil.colorPrint("M2MRestController | $moveName")
        RobotResource.execMove(moveName)
        //TODO: handle the answer to the move execution
        return "done $moveName"
    }

}
//curl -d move=l localhost:8081/moverest

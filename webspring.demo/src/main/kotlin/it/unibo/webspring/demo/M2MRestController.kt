package it.unibo.webspring.demo


import it.unibo.actor0.sysUtil
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class M2MRestController {
    init{
        RobotResource.initRobotResource(true) //we want a local BasicStepRobotActorCaller
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

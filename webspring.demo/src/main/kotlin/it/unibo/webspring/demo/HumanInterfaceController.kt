package it.unibo.webspring.demo

import com.andreapivetta.kolor.Color
import it.unibo.`is`.interfaces.protocols.IConnInteraction
import it.unibo.actor0.sysUtil
import it.unibo.actor0Service.ConnectionReader
import it.unibo.actorAppl.BasicStepRobotActorCaller
import it.unibo.interaction.IUniboActor
import it.unibo.robotService.ApplMsgs
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import it.unibo.robotService.BasicStepRobotActor
import it.unibo.supports.FactoryProtocol
import it.unibo.supports.NaiveActorKotlinObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import org.checkerframework.checker.units.qual.C

@Controller
class HumanInterfaceController {
    @Value("\${human.logo}")
    var appName: String?    = null
    var applicationModelRep = "waiting"

    lateinit var obs         : NaiveActorKotlinObserver
    lateinit var robot       : BasicStepRobotActorCaller
    //lateinit var robotCaller : it.unibo.actorAppl.BasicStepRobotActorCaller

    init{
        initRobotLocal()
        //initRobotCaller()
    }

    fun initRobotLocal(){
        val myscope = CoroutineScope(Dispatchers.Default)
        sysUtil.colorPrint("HumanInterfaceController | init ${sysUtil.curThread()} ", Color.GREEN)
        //TODO: define an observer that updates the HTML page
        obs   = NaiveActorKotlinObserver("obs", 0, myscope)
        //Create a local BasicStepRobotActor => no need for a TCP calls
        BasicStepRobotActor("stepRobot", ownerActor=obs, myscope, "localhost")
        robot = BasicStepRobotActorCaller("robot", myscope )
    }
    fun initRobotCaller(){
        val fp = FactoryProtocol(null, "TCP", "hmi")
        val conn : IConnInteraction = fp.createClientProtocolSupport("localhost", 8010)
        sysUtil.colorPrint("hmi | init connected:$conn", Color.BLUE)

        val myscope = CoroutineScope(Dispatchers.Default)
        sysUtil.colorPrint("HumanInterfaceController | init ${sysUtil.curThread()} ", Color.GREEN)
        obs   = NaiveActorKotlinObserver("obs", 0, myscope)
        robot = BasicStepRobotActorCaller("robot", myscope )
        //val reader = ConnectionReader ("reader", conn )
        //reader.registerActor(owner)	//the answer received by the reader are redirected to the owner
        //reader.send( ApplMsgs.startAny(name))

    }
    @GetMapping("/")    //defines that the method handles GET requests.
    fun entry(model: Model): String {
        model.addAttribute("arg", appName)
        println("HumanInterfaceController | entry model=$model")
        return "naiveRobotGui"
    }

    /*
    Spring provides a Model object which can be passed into the controller.
    You can configure this model object via the addAttribute(key, value) method.
     */

    @GetMapping("/model")
    @ResponseBody   //With this annotation, the String returned by the methods is sent to the browser as plain text.
    fun  homePage( model: Model) : String{
        model.addAttribute("arg", appName)
        sysUtil.colorPrint("HumanInterfaceController | homePage model=$model", Color.GREEN)
        return String.format("HumanInterfaceController text normal state= $applicationModelRep"  );
    }

    //@RequestMapping methods assume @ResponseBody semantics by default.
    //https://springframework.guru/spring-requestmapping-annotation/

    //@RequestMapping( "/move") //, method = RequestMethod.POST,  MediaType.APPLICATION_FORM_URLENCODED_VALUE
    @PostMapping("/move")  //signals that this method handles POST requests
    //@ResponseBody
    fun move( model: Model, @RequestParam(name = "movetodo") move:String ) : String {
        sysUtil.colorPrint("HumanInterfaceController | doMove  $move", Color.GREEN)
        execMove(move)
        return  "naiveRobotGui"
    }

    @PostMapping("/domove")
    fun do_move(model: Model, @RequestParam(name = "movetodo") move:String) : String {
        sysUtil.colorPrint("HumanInterfaceController | do_move $move ", Color.GREEN)
        execMove(move)
        return  "naiveRobotGui"
    }

    fun execMove( move : String ) {
        when (move) {
            "l" -> robot.send(ApplMsgs.stepRobot_l("spring"))
            "r" -> robot.send(ApplMsgs.stepRobot_r("spring"))
            "w" -> robot.send(ApplMsgs.stepRobot_w("spring"))
            "s" -> robot.send(ApplMsgs.stepRobot_s("spring"))
            "p" -> robot.send(ApplMsgs.stepRobot_step("spring", "350"))
        }
    }


}
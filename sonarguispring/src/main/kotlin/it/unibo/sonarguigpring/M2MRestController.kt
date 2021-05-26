package it.unibo.sonarguigpring

import com.andreapivetta.kolor.Color
import it.unibo.actor0.sysUtil
import kotlinx.coroutines.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@kotlinx.coroutines.ObsoleteCoroutinesApi
@RestController
class M2MRestController {
     val myscope = CoroutineScope(Dispatchers.Default)

    init{

    }
/*
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    @PostMapping( "/moverest" )
    fun doMove( @RequestParam(name="move", required=false, defaultValue="h") moveName : String) : String {
        sysUtil.colorPrint("M2MRestController | $moveName | $this")
        var answerMove     = ""
        //RobotResource.execMove(robot, moveName)
        runBlocking{
          }//runBlocking
        sysUtil.colorPrint("result=$answerMove"  , Color.MAGENTA)
        return "$answerMove"
    }
    @PostMapping( "/dopath" )
    fun doPath( @RequestParam(name="path", required=false, defaultValue="") pathTodo : String) : String {
        sysUtil.colorPrint("M2MRestController | pathTodo=$pathTodo")
         return " "
    }

*/

 }//M2MRestController

//curl -d move=l localhost:8081/moverest

//https://code-with-me.jetbrains.com/lQaF6hxwe_Y-lf6az_zO2A#p=IC&fp=28C376F117E242763A56D8152AA07279F60FCAB06A9A2D80EDBFEF8C6737C385
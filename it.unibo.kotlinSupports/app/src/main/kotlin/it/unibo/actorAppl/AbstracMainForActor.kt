package it.unibo.actorAppl

import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor
import kotlinx.coroutines.*

abstract class AbstracMainForActor {

    fun colorPrint(msg : String, color : Color = Color.CYAN ){
        println(Kolor.foreground("      $msg", color ) )
    }

    abstract  fun mainJob( scope: CoroutineScope  )

    fun curThread() : String {
        val nt = Thread.activeCount()
        return "thread=${Thread.currentThread().name} / nthreads=${nt}  "
    }

/*
Hides the runBlocking to the Java level
 */

    fun startmain() {
        runBlocking {
            //println("AbstracMainForActor BEGIN runBlocking ${curThread()}"   )
            mainJob(this)
            //println("AbstracMainForActor END runBlocking ${curThread()}"   )
        }
        //colorPrint("AbstracMainForActor BYE ${curThread()}"   )
    }


}
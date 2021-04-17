package it.unibo.actorAppl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

abstract class AbstracMainForActor {
    protected val myscope = CoroutineScope(Dispatchers.IO)

    abstract fun mainJob()

    fun startmain() {
        runBlocking {
            mainJob()
        }
    }


}
package it.unibo.kactor

import kotlinx.coroutines.GlobalScope

abstract class ActorWrapper(name: String) :
    ActorBasic(name, GlobalScope, false, false, false, 50) {

    override
    suspend fun actorBody( msg: ApplMessage ){
        doJob(msg)
    }

    protected abstract fun doJob(msg: ApplMessage?)
}
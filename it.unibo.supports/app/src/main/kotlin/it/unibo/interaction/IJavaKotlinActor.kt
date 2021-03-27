package it.unibo.interaction

import it.unibo.actor0.ActorBasicKotlin

interface IJavaKotlinActor {
    fun myname() : String
    fun send(msg: String )
    fun registerActor(obs: ActorBasicKotlin)
    fun removeActor(obs: ActorBasicKotlin)
}
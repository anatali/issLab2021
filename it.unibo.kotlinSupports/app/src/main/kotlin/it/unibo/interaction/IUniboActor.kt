package it.unibo.interaction
interface IUniboActor : IssActorObservable  {
    fun myname() : String
    fun send(msg: String )
}
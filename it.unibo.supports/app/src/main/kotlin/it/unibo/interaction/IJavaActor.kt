package it.unibo.interaction
interface IJavaActor : IssActorObservable  {
    fun myname() : String
    fun send(msg: String )
}
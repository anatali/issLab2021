package it.unibo.interaction
interface IJavaActor : IssObservable {
    fun myname() : String
    fun send(msg: String )
}
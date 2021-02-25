/*
ISSActor.kt
 */
package it.unibo.annotExample

abstract class ISSActor @ISSActorSpec constructor() {
    protected var myname = "unknown"

    abstract fun show()

    init {
        myname = UniboAnnotationUtil.getActorName(this.javaClass)
        UniboAnnotationUtil.initializeObject(this) //calls a method of the user class
        println("ISSActor | create $myname")
    }
}
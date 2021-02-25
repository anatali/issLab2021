/**
 * ApplISSActor.java
 * =========================================================================
 * An annotated class that exploits annotations (instead of inheritance)
 * to introduce built-in behavior in declarative way
 * =========================================================================
 */
package it.unibo.annotExample

@ISSActorSpec(actorName = "actor0")
class ApplISSActor : ISSActor() {
    @InitSpec
    override fun show() {
        //super.show();
        println("$myname ApplISSActor | myname= $myname")
    }

    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            ApplISSActor() //.show(); ;
        }
    }
}
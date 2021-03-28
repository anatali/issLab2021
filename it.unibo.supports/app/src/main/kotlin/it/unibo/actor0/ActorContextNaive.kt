package it.unibo.actor0

import kotlinx.coroutines.CoroutineScope


object ActorContextNaive {

    val ctxActorMap = mutableMapOf<String, ActorBasicKotlin>()

    @JvmStatic
    fun getActor(name: String): ActorBasicKotlin? {
        if (ctxActorMap.containsKey(name)) return ctxActorMap.get(name)
        else return null
    }
    @JvmStatic
    fun addActor(a: ActorBasicKotlin) {
        if (!ctxActorMap.containsKey(a.name)) ctxActorMap.put(a.name, a)
    }
    @JvmStatic
    fun createActor(actorName: String, className: String, scope: CoroutineScope): ActorBasicKotlin {
        val clazz = Class.forName(className)    //Class<?>
        var newactor: ActorBasicKotlin
         //println("ActorContextLocal  | createActor actorName ${actorName} ${sysUtil.aboutThreads(ctx.name)}" )
        try {
            //val ctor = clazz.getConstructor(String::class.java, CoroutineScope::class.java )  //Constructor<?>
            val ctor = clazz.getConstructor(
                String::class.java, CoroutineScope::class.java, DispatchType::class.java
            )  //Constructor<?>
            newactor = ctor.newInstance(actorName, scope, DispatchType.single) as ActorBasicKotlin
        } catch (e: Exception) {
            println("ActorContextLocal  | createActor ERROR ${e}")
            val ctor = clazz.getConstructor(String::class.java)  //Constructor<?>
            newactor = ctor.newInstance(actorName) as ActorBasicKotlin
        }
        return newactor
    }

}

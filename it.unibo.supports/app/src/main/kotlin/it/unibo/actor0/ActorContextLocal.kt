package it.unibo.actor0

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ActorContextLocal(name: String, scope: CoroutineScope) :
        ActorBasicKotlin(name, scope, DispatchType.single) {

    companion object {
        val ctxActorMap = mutableMapOf<String, ActorBasicKotlin>()
        var ctx : ActorContextLocal? = null //("localCtx", scope) //? = null

        fun getLocalContext(scope: CoroutineScope)  : ActorContextLocal{
            //return ctx

            if( ctx == null ){
                ctx = ActorContextLocal("localCtx", scope)
                /*
                val startMsg = ApplMessage("startTheActorContextLocal",
                        ApplMessageType.dispatch.toString(), ctx!!.name, ctx!!.name, "init")
                ctx!!.sendToYourself(startMsg)  //create a new thread
                ctx!!.scope.launch{ MsgUtil.sendMsg(startMsg, ctx!!) }*/
            }
            return ctx!!
        }

        fun getActor(name : String ) : ActorBasicKotlin?{
            if( ctxActorMap.containsKey(name)) return ctxActorMap.get(name)
            else return null
        }
        fun addActor(a : ActorBasicKotlin )   {
            if( ! ctxActorMap.containsKey(a.name)) ctxActorMap.put(a.name, a)
            a.ctx = ctx
        }

        fun createActor(actorName: String,className:String,scope:CoroutineScope):ActorBasicKotlin{
            val clazz = Class.forName(className)	//Class<?>
            var newactor : ActorBasicKotlin
            var ctx = getLocalContext(scope)
            //println("ActorContextLocal  | createActor actorName ${actorName} ${sysUtil.aboutThreads(ctx.name)}" )
            try {
                //val ctor = clazz.getConstructor(String::class.java, CoroutineScope::class.java )  //Constructor<?>
                val ctor = clazz.getConstructor(
                        String::class.java, CoroutineScope::class.java, DispatchType::class.java )  //Constructor<?>
                newactor = ctor.newInstance(actorName, scope, DispatchType.single  ) as ActorBasicKotlin
            } catch( e : Exception ){
                println("ActorContextLocal  | createActor ERROR ${e}" )
                val ctor = clazz.getConstructor( String::class.java )  //Constructor<?>
                newactor    = ctor.newInstance( actorName  ) as ActorBasicKotlin
            }
            return newactor
        }
    }//companion
/*
    fun startTheActors(){
        val startMsg = ApplMessage("startTheActor",
                ApplMessageType.dispatch.toString(), Companion.ctx.name, ctx.name, "from_ctx")
        ctxActorMap.forEach{ v : ActorBasicKotlin -> v.actor.send(startMsg)}
    }
*/
    override suspend fun handleInput(msg: ApplMessage) {
        println("ActorContextLocal  | handleInput ${msg} ${sysUtil.aboutThreads(name)}" )
        if( msg.msgId == "startTheActorContextLocal ") return
        if( msg.msgId == "startTheActor ") {
            if( ctxActorMap.contains(msg.msgReceiver))
                this.forward(""," ", ctxActorMap.get(msg.msgReceiver)!! )
        }

    }
}

/*
    fun createActor( actorName: String,
                     className : String, scope : CoroutineScope = GlobalScope  ) : ActorBasicKotlin{
        val clazz = Class.forName(className)	//Class<?>
        var actor  : ActorBasicKotlin
        try {
            val ctor = clazz.getConstructor(String::class.java, CoroutineScope::class.java )  //Constructor<?>
            actor = ctor.newInstance(actorName, scope  ) as ActorBasicKotlin
            val startMsg = ApplMessage("startTheActor",
                    ApplMessageType.dispatch.toString(), actor.name, actor.name, "actorContext")
            actor.sendToYourself(startMsg)
        } catch( e : Exception ){
            //println("sysUtil  | ERROR ${e}" )
            val ctor = clazz.getConstructor( String::class.java )  //Constructor<?>
            actor = ctor.newInstance( actorName  ) as ActorBasicKotlin
        }
        //ctx.addActor(actor)
        //actor.context = ctx
        //MEMO THE ACTOR
        //ctxActorMap.put(actorName,actor  )
        return actor
    }
*/

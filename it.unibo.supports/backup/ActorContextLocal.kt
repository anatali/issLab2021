package it.unibo.actor0

import kotlinx.coroutines.CoroutineScope


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

    override  fun handleInput(msg: ApplMessage) {
        println("ActorContextLocal  | handleInput ${msg} ${sysUtil.aboutThreads(name)}" )
        /*
        if( msg.msgId == "startTheActorContextLocal ") return
        if( msg.msgId == "startTheActor ") {
            if( ctxActorMap.contains(msg.msgReceiver)){
                //start the actor names msgReceiver
                scope.launch{ ctxActorMap.get(msg.msgReceiver)!!.sendToYourself()}
            }
               // this.forward(""," ", ctxActorMap.get(msg.msgReceiver)!! )
        }
*/
    }
}


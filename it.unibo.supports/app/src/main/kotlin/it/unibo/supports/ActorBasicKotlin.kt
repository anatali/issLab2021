package it.unibo.supports

import it.unibo.interaction.IJavaActor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import java.util.Vector
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.lang.InterruptedException
import java.util.function.Consumer
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class ActorBasicKotlin( val myname: String  ) {
    //private val kactor = CoroutineScope( Dispatchers.Default ).actor
    private var goon = true
    private val actorobservers = Vector<IJavaActor>()
    private val bqueue: BlockingQueue<String> = LinkedBlockingQueue(10)

    //init {
        //name = "th_$myname" //set the name of the thread
        //start()
    //}
    //------------------------------------------------
    //Utility ops to communicate with another, known actor
    protected fun forward(msg: String, dest: ActorBasicKotlin) {
        dest.send(msg)
    }

    protected fun request(msg: String, dest: ActorBasicKotlin) {
        dest.send(msg)
    }

    protected fun reply(msg: String, dest: ActorBasicKotlin) {
        dest.send(msg)
    }

      fun myname(): String {
        return myname
    }

    /*
    @Override
    public void activate(){
        send("system_start_msg");   //automsg
        start();
    }*/
    @Synchronized
      fun send(msg: String) {
        try {
            bqueue.put(msg)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    //------------------------------------------------
      fun run() {
        while (goon) {
            waitInputAndElab()
        }
        //System.out.println( name + " | waitInputAndElab END "  );
    }

    fun terminate() {
        //System.out.println(name +  " | terminate "   );
        goon = false
        send("bye")
    }

    protected fun waitInputAndElab() {
        try {
            //System.out.println(name +  " | waitInputAndElab ... "  );
            //val info = bqueue.take()
            //if (goon) handleInput(info)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //protected abstract fun handleInput(info: String?)

    //---------------------------------------------------------------------------
      fun registerActor(obs: IJavaActor) {
        actorobservers.add(obs)
    }

      fun removeActor(obs: IJavaActor) {
        actorobservers.remove(obs)
    }

    protected fun updateObservers(info: String?) {
        //System.out.println(name + " update " + actorobservers.size() );
        actorobservers.forEach(Consumer { v: IJavaActor -> v.send(info!!) })
    }

    companion object {
        //-----------------------------------------------------
        fun aboutThreads(): String {
            return "curThread="
        }


    }


}
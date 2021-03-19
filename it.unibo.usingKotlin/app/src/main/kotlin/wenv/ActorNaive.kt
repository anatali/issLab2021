/**
 * ActorNaive.java
 * ===============================================================
 * A kotlin class that extends the traditional Java Thread
 * that works as a non-blocking (WEnv) observer
 * in a message-driven way while goon=true
 *
 * WARNING: this is for a 'graceful transition' to Kotlin
 * IT IS NOT the Kotlin way to face message-driven components
 * ===============================================================
 *
 */
package wenv
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class ActorNaive : Thread() {
    private var goon = true
    private val bqueue: BlockingQueue<String> = LinkedBlockingQueue(10)

    override fun run() {
        while (goon) waitInputAndElab()
    }
    fun terminate() {
        println("ActorNaive | ENDS")
        goon = false
        put("bye")
    }

    fun put(info: String) {
        try {
            bqueue.put(info)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    protected fun waitInputAndElab() {
        try {
            val info = bqueue.take()
            if (goon) handleInput( info )
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
    protected fun handleInput(info: String) {
        println("ActorNaive | ------------ $info")
    }
}
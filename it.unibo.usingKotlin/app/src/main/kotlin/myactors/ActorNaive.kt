/**
 * ActorNaive.java
 * ===============================================================
 * ===============================================================
 *
 */
package myactors

import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class ActorNaive : Thread() {
    private val info: String? = null
    private var goon = true
    private val queue = Vector<String>()
    private val bqueue: BlockingQueue<String> = LinkedBlockingQueue(10)

    override fun run() {
        while (goon) waitInputAndElab()
    }

    fun terminate() {
        goon = false
    }

    @Synchronized
    fun put(info: String) {
        try {
            bqueue.put(info)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    @Synchronized
    protected fun waitInputAndElab() {
        try {
            val info = bqueue.take()
            if (goon) handleInput(queue.removeAt(0))
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    protected fun handleInput(info: String) {
        println("ActorNaive | ------------ $info")
    }
}
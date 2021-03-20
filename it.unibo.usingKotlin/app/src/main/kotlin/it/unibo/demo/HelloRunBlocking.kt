/**
 * HelloRunBlocking
 * @author AN - DISI - Unibo
===============================================================
Just to show the initial number of threads is 2 and does not change
===============================================================
 */
package it.unibo.demo
import kotlinx.coroutines.runBlocking

class HelloRunBlocking(){
    fun doJob(){
        println("HelloRunBlocking | doJob n_Threads=" + Thread.activeCount());
    }
}
    fun main() = runBlocking {
        println("==============================================")
        println("HelloRunBlocking | main START n_Threads=" + Thread.activeCount());
        println("==============================================")
        HelloRunBlocking().doJob();
        println("==============================================")
        println("HelloRunBlocking | main END n_Threads=" + Thread.activeCount());
        println("==============================================")
    }
